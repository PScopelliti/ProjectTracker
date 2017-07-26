package app.attempt

import app.v1.model.Note

trait ServiceComponent {

  def noteService: NoteService

  trait NoteService {
    def createNote: Note
  }

}
