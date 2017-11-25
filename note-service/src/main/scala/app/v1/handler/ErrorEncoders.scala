package app.v1.handler

import io.finch.Encode
import app.v1.handler.ExceptionEncoder._

trait ErrorEncoders {
  implicit val exceptionEncode: Encode.Json[Exception] = ResponseOps.exceptionJsonEncode(exceptionEncoder)
  implicit val throwableEncode: Encode.Json[Throwable] = ResponseOps.throwableJsonEncode(throwableEncoder)
}

object ErrorEncoders extends ErrorEncoders
