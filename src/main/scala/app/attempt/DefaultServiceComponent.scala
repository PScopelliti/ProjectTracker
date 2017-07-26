package app.attempt

import app.v1.model.Note

trait DefaultServiceComponent extends ServiceComponent {

  this: UUIDComponent =>

  def noteService = new DefaultNoteService

  class DefaultNoteService extends NoteService {
    def createNote: Note = Note(noteUUID.getUUID, "test 1")
  }

}
