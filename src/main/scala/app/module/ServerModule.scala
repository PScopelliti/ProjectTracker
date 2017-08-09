package app.module

import app.config.application.ApplicationProperty
import app.v1.Api
import com.twitter.finagle.Http
import com.twitter.logging.Logger
import com.twitter.util.{ Await, Duration, StorageUnit }

trait ServerModule {

  self: com.twitter.app.App with Api with ApplicationProperty =>

  private val log = Logger.get(getClass)

  premain {
    log.info("[Finch] server is starting ...")

    val server = Http.server
      .withLabel(applicationProperty.systemId)
      .withRequestTimeout(Duration.fromSeconds(applicationProperty.requestTimeoutInSeconds))
      .withMaxRequestSize(StorageUnit.fromMegabytes(applicationProperty.maxRequestSizeInMB))
      .configured(Http.Netty4Impl)
      .serve(s":${applicationProperty.applicationPort}", apiService)

    onExit {
      server.close()
    }

    Await.result(server)
  }

}
