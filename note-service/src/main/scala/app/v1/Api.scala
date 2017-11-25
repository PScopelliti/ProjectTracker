package app.v1

import app.filter.{ RequestLoggingFilter, RoutingMetricsFilter }
import app.metrics.Metrics.serverMetrics
import app.v1.api.NoteApi
import app.v1.handler.FinchTemplateErrorHandler.apiErrorHandler
import app.v1.handler.{ ExceptionFilter, FinchTemplateErrorHandler, ResponseEncoders }
import com.twitter.finagle.Service
import com.twitter.finagle.http.{ Request, Response }
import io.circe.generic.auto._
import io.finch.circe._

object UnhandledExceptionsFilter extends ExceptionFilter(ResponseEncoders.throwableEncode, FinchTemplateErrorHandler, serverMetrics)

object RouteMetricsFilter extends RoutingMetricsFilter(serverMetrics)

trait Api {

  self: NoteApi =>

  private def api = noteApi.endpoints

  private val baseFilter = RequestLoggingFilter andThen UnhandledExceptionsFilter andThen RouteMetricsFilter

  import app.v1.model.Note._
  def apiService: Service[Request, Response] = baseFilter andThen api.handle(apiErrorHandler).toService

}
