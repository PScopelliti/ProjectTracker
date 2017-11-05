package app.module

import app.config.application.ApplicationProperty
import app.v1.Api
import com.twitter.finagle.Http
import com.twitter.finagle.Http.Netty4Impl
import com.twitter.logging.Logger
import com.twitter.util.Await
import com.twitter.util.Duration.fromSeconds
import com.twitter.util.StorageUnit.fromMegabytes

trait ServerModule {

  self: com.twitter.app.App with Api with ApplicationProperty =>

  private val log = Logger.get(getClass)

  def welcomeBanner: List[String] =
    List(
      """                                 _ _                """,
      """              ____ __  _ _  _ _ (_| )_              """,
      """-------------| __// _)| '_)| '_)| | |_--------------""",
      """-------------| _| \__)|_|--|_|--|_|\__)-------------""",
      """=============|_|====================================""",
      """                                                    """,
      """------------ THE  W E B  C R A W L E R -------------""",
      ""
    )

  premain {

    welcomeBanner.foreach(log.info(_))
    log.info("[Finch] server is starting ...")

    val server = Http.server
      .withLabel(applicationProperty.systemId)
      .withRequestTimeout(fromSeconds(applicationProperty.requestTimeoutInSeconds))
      .withMaxRequestSize(fromMegabytes(applicationProperty.maxRequestSizeInMB))
      .configured(Netty4Impl)
      .serve(s":${applicationProperty.applicationPort}", apiService)

    onExit {
      server.close()
    }

    Await.result(server)
  }

}
