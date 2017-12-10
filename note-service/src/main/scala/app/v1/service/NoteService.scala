package app.v1.service

import app.v1.model.Note
import com.outworkers.phantom.finagle._
import com.twitter.util.Future

trait NoteService {

  def findById(id: UUID): Future[Option[Note]]

  def store(id: UUID, note: Note): Future[Note]

  def updateItem(id: UUID, note: Note): Future[Note]

  def deleteItem(id: UUID): Future[Unit]

  def getItems(): Future[List[Note]]
}
