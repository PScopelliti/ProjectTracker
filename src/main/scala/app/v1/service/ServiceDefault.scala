package app.v1.service

import java.util.UUID

import app.v1.model.Note

trait ServiceDefault extends ServiceComponent {

  this: UUIDComponent =>

  def noteService = new DefaultNoteService

  class DefaultNoteService extends NoteService {

    def createNote(noteGen: UUID => Note): Note = noteGen(noteUUID.getUUID)

    def getNotes: List[Note] = List(Note(noteUUID.getUUID, "Note 1"), Note(noteUUID.getUUID, "Note 2"))

    def getNoteById(uuid: UUID): Note = Note(uuid, "Note 1")

    def deleteNote(uuid: UUID): Unit = 0

    def patchNote(uuid: UUID, note: Note): Note = note
  }

}
