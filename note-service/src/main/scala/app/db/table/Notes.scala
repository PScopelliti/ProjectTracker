package app.db.table

import app.db.RootConnector
import app.v1.model.Note
import com.outworkers.phantom.finagle.{UUID, _}
import com.twitter.util.Future

abstract class Notes extends Table[Notes, Note] with RootConnector {

  object id extends UUIDColumn with PartitionKey

  object userId extends UUIDColumn

  object content extends StringColumn

  object date extends DateTimeColumn

  def findById(noteId: UUID): Future[Option[Note]] = {
    select.where(_.id eqs noteId).one()
  }

  def store(note: Note): Future[ResultSet] = {
    insert
      .value(_.id, note.id)
      .value(_.userId, note.userid)
      .value(_.content, note.content)
      .value(_.date,note.date)
      .future()
  }
}