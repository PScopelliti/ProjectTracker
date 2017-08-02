package app.v1.service

import java.util.UUID

import app.v1.model.Note
import com.twitter.logging.Logger

trait ServiceDefault extends ServiceComponent {

  self: UUIDComponent =>

  private val log = Logger.get(getClass)

  val noteService: DefaultNoteService = new DefaultNoteService

  class DefaultNoteService extends NoteService {

    def createNote(noteGen: UUID => Note): Note = {
      log.info("Calling createNote service... ")
      noteGen(noteUUID.getUUID)
    }

    def getNotes: List[Note] = {
      log.info("Calling getNotes service... ")
      List(Note(noteUUID.getUUID, "Note 1"), Note(noteUUID.getUUID, "Note 2"))
    }

    def getNoteById(uuid: UUID): Note = {
      log.info("Calling getNoteById service... ")
      Note(uuid, "Note 1")
    }

    def deleteNote(uuid: UUID): Unit = {
      log.info("Calling deleteNote service... ")
      0
    }

    def patchNote(uuid: UUID, note: Note): Note = {
      log.info("Calling patchNote service... ")
      note
    }
  }

}
