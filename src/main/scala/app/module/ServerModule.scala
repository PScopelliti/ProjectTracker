package app.module

import app.Main.statsReceiver
import app.v1.Api
import com.twitter.finagle.Http
import com.twitter.finagle.param.Stats
import com.twitter.logging.Logger
import com.twitter.util.Await

trait ServerModule {

  self: com.twitter.app.App with Api =>

  private val log = Logger.get(getClass)

  premain {
    log.info("[Finch] server is starting ...")

    val server = Http.server
      .configured(Stats(statsReceiver))
      .serve(":8081", apiService)

    onExit {
      server.close()
    }

    Await.result(server)
  }

}
