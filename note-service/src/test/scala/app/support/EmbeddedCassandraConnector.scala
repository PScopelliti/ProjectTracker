package app.support

import app.config.ConfigurationLoader
import app.config.datastore.CassandraDBProperty
import com.outworkers.phantom.connectors.{ CassandraConnection, ContactPoint }

trait EmbeddedCassandraConnector
  extends CassandraDBProperty
  with ConfigurationLoader {

  val connector: CassandraConnection =
    ContactPoint(9142)
      .noHeartbeat()
      .keySpace("notespace")
}

object EmbeddedCassandraConnector extends EmbeddedCassandraConnector