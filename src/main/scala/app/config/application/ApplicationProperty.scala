package app.config.application

import app.config.ConfigurationLoader
import com.twitter.util.Duration

trait ApplicationProperty {

  self: ConfigurationLoader =>

  def applicationProperty: ApplicationProperty = new ApplicationProperty

  class ApplicationProperty {

    def applicationPort: String = configuration.getString("app.port")

    def systemId: String = configuration.getString("app.systemId")

    def requestTimeoutInSeconds: Int = configuration.getInt("app.requestTimeoutInSeconds")

    // Reject requests greater than this size.
    def maxRequestSizeInMB: Int = configuration.getInt("app.maxRequestSizeInMB")
  }

}