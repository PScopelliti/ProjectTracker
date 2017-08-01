package app

import app.module.ServerModule
import app.utils.AppLogger
import app.v1.Api
import app.v1.handler.ErrorHandler
import com.twitter.server.TwitterServer
import com.twitter.util.Await

object Main
  extends TwitterServer
    with ServerModule
    with AppLogger
    with Api
    with ErrorHandler {

  def main(): Unit = {
    Await.result(adminHttpServer)
  }

}
