package app.v1.service

import java.util.UUID

import app.v1.model.Note
import com.twitter.util.Future

trait ServiceComponent {

  val noteService: NoteService

  trait NoteService {

    def createNote(noteGen: UUID => Note): Future[Note]

    def getNoteById(uuid: UUID): Future[Note]

    def deleteNote(uuid: UUID): Future[Boolean]

    def patchNote(uuid: UUID, note: Note): Note

  }

}
