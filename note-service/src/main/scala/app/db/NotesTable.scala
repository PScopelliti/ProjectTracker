package app.db

import java.util.UUID

import com.outworkers.phantom.finagle._

import app.v1.model.Note
import com.twitter.util.Future

abstract class NotesTable extends Table[NotesTable, Note] with RootConnector {

  object noteId extends UUIDColumn with PartitionKey

  object userId extends UUIDColumn

  object content extends StringColumn

  object created extends DateTimeColumn

  def findById(noteId: UUID): Future[Option[Note]] = {
    select.where(_.noteId eqs noteId).one()
  }
}