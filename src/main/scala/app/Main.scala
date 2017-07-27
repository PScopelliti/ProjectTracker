package app

import app.module.ServerModule
import com.twitter.server.TwitterServer
import com.twitter.util.Await


object Main extends TwitterServer with ServerModule {

  def main(): Unit = {
    Await.result(adminHttpServer)
  }

}
