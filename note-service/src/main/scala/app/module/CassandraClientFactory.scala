package app.module

import app.config.ConfigurationLoader
import app.config.datastore.CassandraDBProperty
import com.datastax.driver.core.{ Cluster, Session }
import com.google.common.util.concurrent.ListenableFuture

object CassandraClientFactory
  extends CassandraDBProperty
  with ConfigurationLoader {

  private val cluster = Cluster.builder()
    .addContactPoint(dbProperties.url)
    .withPort(dbProperties.port)
    .build()

  lazy val session: ListenableFuture[Session] = cluster.connectAsync(dbProperties.noteKeyspace)

}
