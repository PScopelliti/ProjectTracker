package app.v1

import app.v1.api.NoteApiWiring
import app.v1.handler.ErrorHandler
import com.twitter.finagle.Service
import com.twitter.finagle.http.filter.ExceptionFilter
import com.twitter.finagle.http.{ Request, Response }
import io.circe.generic.auto._
import io.finch.circe._

trait Api {

  self: ErrorHandler =>

  private def api = NoteApiWiring.noteApi.endpoints

  def apiService: Service[Request, Response] = ExceptionFilter andThen api.handle(apiErrorHandler).toService

}
