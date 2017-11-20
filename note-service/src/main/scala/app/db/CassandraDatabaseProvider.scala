package app.db

import com.outworkers.phantom.finagle.DatabaseProvider

trait CassandraDatabaseProvider extends DatabaseProvider[NoteDatabase] {
  override def database: NoteDatabase = new NoteDatabase(CassandraConnector.connector)
}