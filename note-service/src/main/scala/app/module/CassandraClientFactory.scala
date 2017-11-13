package app.module

import app.config.ConfigurationLoader
import app.config.datastore.CassandraDBProperty
import com.datastax.driver.core.{Cluster, Session}

object CassandraClientFactory
  extends CassandraDBProperty
    with ConfigurationLoader {

  def session: Session = cluster.connect(dbProperties.noteKeyspace)

  private def cluster = Cluster.builder()
    .addContactPoint(dbProperties.url)
    .withPort(dbProperties.port)
    .build()

}
