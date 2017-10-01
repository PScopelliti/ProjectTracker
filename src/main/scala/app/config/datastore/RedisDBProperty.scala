package app.config.datastore

import app.config.ConfigurationLoader

trait RedisDBProperty {

  self: ConfigurationLoader =>

  val dbProperties = new RedisDBProperty

  class RedisDBProperty {

    def url: String = configuration.getString("redis.url")

    def port: String = configuration.getString("redis.port")
  }

}