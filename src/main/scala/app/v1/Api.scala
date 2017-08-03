package app.v1

import app.filter.{ RequestLoggingFilter, RoutingMetricsFilter }
import app.metrics.Metrics.serverMetrics
import app.v1.api.NoteApi
import app.v1.handler.ErrorHandler
import com.twitter.finagle.Service
import com.twitter.finagle.http.filter.ExceptionFilter
import com.twitter.finagle.http.{ Request, Response }
import io.circe.generic.auto._
import io.finch.circe._

object RouteMetricsFilter extends RoutingMetricsFilter(serverMetrics)

trait Api {

  self: ErrorHandler with NoteApi =>

  private def api = noteApi.endpoints

  private val baseFilter = RequestLoggingFilter andThen ExceptionFilter andThen RouteMetricsFilter

  def apiService: Service[Request, Response] = baseFilter andThen api.handle(apiErrorHandler).toService

}
