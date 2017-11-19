package app.v1.service

import java.util.UUID

import app.v1.model.Note
import com.twitter.util.Future

trait NoteServiceRepository {

  def findById(uuid: UUID): Future[Option[Note]]
}
