package app.v1.service

import app.db.NoteDatabase
import app.v1.model.Note
import com.outworkers.phantom.finagle._
import com.twitter.util.Future

trait NoteService extends DatabaseProvider[NoteDatabase] {

  def findById(id: UUID): Future[Option[Note]] = db.notes.findById(id)

  def store(id: UUID, note: Note): Future[ResultSet] = db.notes.store(id, note)

  def updateItem(id: UUID, note: Note): Future[ResultSet] = db.notes.updateItem(id, note.content)
}