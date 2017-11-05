package app.config.datastore

import app.config.ConfigurationLoader

trait CassandraDBProperty {

  self: ConfigurationLoader =>

  val dbProperties = new CassandraDBProperty

  class CassandraDBProperty {

    def url: String = configuration.getString("cassandra.url")

    def port: Int = configuration.getInt("cassandra.port")

    def noteKeyspace: String = configuration.getString("cassandra.keyspace")
  }

}