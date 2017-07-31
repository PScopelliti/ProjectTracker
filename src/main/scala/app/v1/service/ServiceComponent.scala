package app.v1.service

import java.util.UUID

import app.v1.model.Note

trait ServiceComponent {

  val noteService: NoteService

  trait NoteService {

    def createNote(noteGen: UUID => Note): Note

    def getNotes: List[Note]

    def getNoteById(uuid: UUID): Note

    def deleteNote(uuid: UUID): Unit

    def patchNote(uuid: UUID, note: Note): Note

  }

}
