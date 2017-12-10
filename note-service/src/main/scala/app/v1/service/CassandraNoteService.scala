package app.v1.service

import app.db.NoteDatabase
import app.v1.model.Note
import com.outworkers.phantom.finagle.{ DatabaseProvider, UUID }
import com.twitter.util.Future

trait CassandraNoteService extends NoteService with DatabaseProvider[NoteDatabase] {

  def findById(id: UUID): Future[Option[Note]] = db.notes.findById(id)

  def store(id: UUID, note: Note): Future[Note] = {
    db.notes.store(id, note)
      .flatMap(_ => findById(id))
      .flatMap(noteOpt => Future(noteOpt.get))
  }

  def updateItem(id: UUID, note: Note): Future[Note] = {
    db.notes.updateItem(id, note.content)
      .flatMap(_ => findById(id))
      .flatMap(noteOpt => Future(noteOpt.get))
  }

  def deleteItem(id: UUID): Future[Unit] = {
    db.notes.deleteItem(id)
      .flatMap(_ => Future.Done)
  }

  def getItems(): Future[List[Note]] = {
    db.notes.getItems
      .flatMap(x => Future(x.records))
  }

}