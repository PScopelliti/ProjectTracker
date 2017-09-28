package app.v1.service

import java.util.UUID

import app.v1.model.Note
import com.twitter.util.Future

trait NoteServiceRepository {

  def get(uuid: UUID): Future[Note]

  def set(uuid: UUID, note: Note): Future[Unit]

  def delete(uuid: UUID): Future[Boolean]

}
