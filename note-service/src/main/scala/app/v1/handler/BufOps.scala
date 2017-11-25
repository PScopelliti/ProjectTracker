package app.v1.handler

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.charset.{ Charset => NioCharset }
import java.nio.{ ByteBuffer, CharBuffer }

import com.twitter.finagle.netty3.ChannelBufferBuf.Owned
import com.twitter.io.Buf
import com.twitter.io.Charsets.encoder

trait BufOps {
  final def bufToByteArray(buf: Buf): Array[Byte] = {
    val extracted = Owned.extract(buf)
    val rawArray = extracted.array()
    val size = extracted.readableBytes()
    if (rawArray.length == size) {
      rawArray
    } else {
      val dst = new Array[Byte](size)
      System.arraycopy(extracted.array(), 0, dst, 0, size)
      dst
    }
  }

  final def bufToByteBuffer(buf: Buf): ByteBuffer = Owned.extract(buf).toByteBuffer()

  final def byteBufferToBuf(bytes: ByteBuffer): Buf = Buf.ByteBuffer.Owned(bytes)

  final def stringToBuf(s: String, charset: NioCharset = UTF_8): Buf = {
    val cb = CharBuffer.wrap(s.toCharArray)
    val encoded = encoder(charset).encode(cb)
    byteBufferToBuf(encoded)
  }

  final def bufToString(buf: Buf, charset: NioCharset = UTF_8): String = {
    val output = new Array[Byte](buf.length)
    buf.write(output, 0)
    new String(output, charset)
  }
}

object BufOps extends BufOps
