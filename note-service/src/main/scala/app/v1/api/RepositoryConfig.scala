package app.v1.api

import app.module.CassandraClientFactory
import app.v1.service.{ CassandraNodeServiceRepository, NoteServiceRepository }
import com.datastax.driver.core.Session
import com.google.common.util.concurrent.ListenableFuture

trait RepositoryConfig {

  implicit def session: ListenableFuture[Session] = CassandraClientFactory.session

  implicit def noteServiceRepository: NoteServiceRepository = new CassandraNodeServiceRepository

}