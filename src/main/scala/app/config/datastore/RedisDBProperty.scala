package app.config.datastore

import app.config.ConfigurationLoader

trait RedisDBProperty {

  self: ConfigurationLoader =>

  val dbProperties = new RedisDBProperty

  class RedisDBProperty {

    def settingsTableName: String = configuration.getString("dynamodb.table.settings.name")


    def settingsTableKey: String = configuration.getString("dynamodb.table.settings.key")
  }

}