package app.config.application

import app.config.ConfigurationLoader

trait ApplicationProperty {

  self: ConfigurationLoader =>

  def applicationProperty: ApplicationProperty = new ApplicationProperty

  class ApplicationProperty {
    def applicationPort: String = configuration.getString("app.port")
  }

}