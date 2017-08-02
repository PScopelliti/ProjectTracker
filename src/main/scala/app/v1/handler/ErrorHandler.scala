package app.v1.handler

import com.twitter.finagle.http.{ Request, Response, Status, Version }
import com.twitter.logging.Logger
import com.twitter.util.Future
import io.finch.Error.{ NotParsed, NotPresent, NotValid }
import io.finch._

trait ErrorHandler {

  private val log = Logger.get(getClass)

  def apiErrorHandler: PartialFunction[Throwable, Output[Nothing]] = {
    case e: NotPresent => BadRequest(e)
    case e: NotParsed  => BadRequest(e)
    case e: NotValid   => BadRequest(e)
    case e: Exception  => InternalServerError(e)
  }

  def topLevelErrorHandler[REQUEST <: Request](request: REQUEST, encoder: Encode[Throwable]): PartialFunction[Throwable, Future[Response]] = {
    case t: Throwable => unhandledException(request, t, encoder)
  }

  private def unhandledException[REQUEST <: Request](request: REQUEST, t: Throwable, encoder: Encode[Throwable]): Future[Response] = {
    try {
      log.info(s"Unhandled exception on URI ${request.uri} with message $t")
      Future.value(Response(Version.Http11, Status.InternalServerError))
    } catch {
      case e: Throwable => {
        log.error(s"Unable to log unhandled exception: $e")
        throw e
      }
    }
  }
}