package app.v1.handler

import java.nio.charset.StandardCharsets.UTF_8

import app.v1.handler.BufOps._
import cats.Show
import com.twitter.concurrent.AsyncStream
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.{ Fields, Response, Status, Version }
import com.twitter.io.Buf
import com.twitter.util.Future
import io.circe.{ Encoder, Json }
import io.finch.{ Encode, Text }

trait ResponseOps extends JsonPrinter {
  implicit val bufShow: Show[Buf] = ShowOps.showBuf

  final def jsonBuf[A](a: A)(implicit encoder: Encode.Json[A]): Buf = encoder.apply(a, UTF_8)

  final def htmlEncode[A](implicit s: Show[A]): Encode.Aux[A, Text.Html] =
    Encode.instance[A, Text.Html]((a, charset) => stringToBuf(s.show(a), charset))

  /**
   * Encode a response, where the payload is placed as the root node in the returned JSON, so without an enclosing
   * `data` field. Use this function sparingly, only for `Json` that already includes the field. In general, we always
   * want to return a top-level data element.
   */
  final def rootJsonEncode[A](implicit encoder: Encoder[A]): Encode.Json[A] =
    Encode.json { (a, _) =>
      byteBufferToBuf(jsonToByteBuffer(encoder.apply(a)))
    }

  /**
   * Encode a response, with an enclosing `data` field at the root of the returned JSON.
   */
  final def dataJsonEncode[A](implicit encoder: Encoder[A]): Encode.Json[A] =
    Encode.json { (a, _) =>
      byteBufferToBuf(jsonToByteBuffer(Json.obj("data" -> encoder.apply(a))))
    }

  final def exceptionJsonEncode(implicit encoder: Encoder[Exception]): Encode.Json[Exception] =
    Encode.json { (e, _) =>
      byteBufferToBuf(jsonToByteBuffer(Json.obj("errors" -> Json.arr(encoder.apply(e)))))
    }

  final def throwableJsonEncode(implicit encoder: Encoder[Throwable]): Encode.Json[Throwable] =
    Encode.json { (e, _) =>
      byteBufferToBuf(jsonToByteBuffer(Json.obj("errors" -> Json.arr(encoder.apply(e)))))
    }

  final def jsonResponse[A](status: Status, a: A, version: Version = Http11)(implicit encode: Encode.Json[A]): Response = {
    val response = newResponse(status, version)
    response.setContentTypeJson()
    response.content = jsonBuf(a)
    response
  }

  final def textResponse(status: Status, content: Buf, version: Version = Http11): Response = {
    val response = newResponse(status, version)
    response.headerMap.add(Fields.ContentLength, content.length.toString)
    response.headerMap.add(Fields.ContentLanguage, "en")
    response.headerMap.add(Fields.ContentType, "text/plain")
    response.content = content
    response
  }

  final def htmlResponse(status: Status, content: AsyncStream[Buf], version: Version = Http11): Response = {
    val response = newResponse(status, version)
    response.headerMap.add(Fields.ContentLanguage, "en")
    response.headerMap.add(Fields.ContentType, "text/html")
    response.setChunked(true)
    val writable = response.writer
    var length = 0
    content.foreachF { chunk =>
      length += chunk.length
      writable.write(chunk)
    }.ensure {
      writable.close()
      ()
    }
    response.headerMap.add(Fields.ContentLength, length.toString)
    response
  }

  final def unauthorised(message: String): Future[Response] =
    Future.value(ResponseOps.textResponse(Status.Unauthorized, Buf.Utf8(message)))

  private def newResponse(status: Status, version: Version) = {
    val response = Response()
    response.status = status
    response.version = version
    response.date = ""
    response
  }
}

object ResponseOps extends ResponseOps
