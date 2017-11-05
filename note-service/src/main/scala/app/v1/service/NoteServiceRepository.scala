package app.v1.service

import java.util.UUID

import app.v1.model.Note
import com.twitter.util.Future

trait NoteServiceRepository {

  def getNote(uuid: UUID): Future[Option[Note]]

  def setNote(uuid: UUID, note: Note): Future[Note]

  def deleteNote(uuid: UUID): Future[Boolean]

  def updateNote(uuid: UUID, note: Note): Future[Option[Note]]

  def getAllNote(): Future[List[Note]]
}
