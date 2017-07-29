package app.config

import com.typesafe.config.ConfigFactory

import scala.util.Try

trait Configuration {

  /**
   * Application config object.
   */
  val config = ConfigFactory.load()

  /** Host name/address to start service on. */
  lazy val serviceHost = Try(config.getString("service.host")).getOrElse("localhost")

  /** Port to start service on. */
  lazy val servicePort = Try(config.getInt("service.port")).getOrElse(8080)

}
