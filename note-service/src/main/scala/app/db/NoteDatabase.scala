package app.db

import app.db.table.Notes
import com.outworkers.phantom.finagle._

class NoteDatabase(override val connector: CassandraConnection) extends Database[NoteDatabase](connector) {

  object notes extends Notes with Connector

}