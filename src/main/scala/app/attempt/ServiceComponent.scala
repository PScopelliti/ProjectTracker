package app.attempt

import java.util.UUID

import app.v1.model.Note

trait ServiceComponent {

  def noteService: NoteService

  trait NoteService {
    def createNote(noteGen: UUID => Note): Note
  }

}
