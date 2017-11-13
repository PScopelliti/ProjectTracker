package app.v1.api

import app.module.CassandraClientFactory
import app.v1.service.{CassandraNoteServiceRepository, NoteServiceRepository}
import com.datastax.driver.core.Session

trait RepositoryConfig {

  implicit def session: Session = CassandraClientFactory.session

  implicit def noteServiceRepository: NoteServiceRepository = new CassandraNoteServiceRepository

}