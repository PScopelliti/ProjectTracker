package app.config

import com.typesafe.config.ConfigFactory

trait ConfigurationLoader {

  val configuration = ConfigFactory.load()

}
