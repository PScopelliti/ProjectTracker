package app.attempt

import java.util.UUID

import app.v1.model.Note

trait ServiceDefault extends ServiceComponent {

  this: UUIDComponent =>

  def noteService = new DefaultNoteService

  class DefaultNoteService extends NoteService {
    def createNote(noteGen: UUID => Note): Note = noteGen(noteUUID.getUUID)
  }

}
