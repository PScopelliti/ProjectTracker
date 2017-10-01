package app.v1.service

import java.util.UUID

import app.v1.model.Note
import com.twitter.util.Future

trait NoteServiceRepository {

  def getItem(uuid: UUID): Future[Option[Note]]

  def setItem(uuid: UUID, note: Note): Future[Note]

  def deleteItem(uuid: UUID): Future[Boolean]

}
