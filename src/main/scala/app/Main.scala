package app

import app.module.ServerModule
import app.v1.Api
import app.v1.api.NoteApi
import app.v1.handler.ErrorHandler
import app.v1.service.{ ServiceDefault, UUIDRandom }
import com.twitter.server._

object Main
  extends TwitterServer
  with ServerModule
  with Api
  with NoteApi
  with AdminHttpServer
  with Admin
  with Lifecycle
  with ServiceDefault
  with UUIDRandom
  with ErrorHandler {

}
