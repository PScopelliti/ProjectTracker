package app.db


import com.outworkers.phantom.finagle._

class NoteDatabase(override val connector: CassandraConnection) extends Database[NoteDatabase](connector) {

  object notes extends NotesTable with Connector

}