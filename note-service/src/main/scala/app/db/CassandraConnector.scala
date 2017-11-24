package app.db

import app.config.ConfigurationLoader
import app.config.datastore.CassandraDBProperty
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.finagle.ContactPoint

trait CassandraConnector
  extends CassandraDBProperty
  with ConfigurationLoader {

  val connector: CassandraConnection =
    ContactPoint(dbProperties.port)
      .noHeartbeat()
      .keySpace(dbProperties.noteKeyspace)
}

object CassandraConnector extends CassandraConnector