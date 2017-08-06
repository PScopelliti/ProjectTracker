package app.v1.service

import java.util.UUID

import app.module.RedisClientModule
import app.v1.model.Note
import com.twitter.finagle.redis.util.StringToBuf
import com.twitter.logging.Logger
import com.twitter.util.Future
import io.circe.generic.auto._
import io.circe.syntax._

trait ServiceDefault extends ServiceComponent {

  self: UUIDComponent with RedisClientModule =>

  private val log = Logger.get(getClass)

  val noteService: DefaultNoteService = new DefaultNoteService

  class DefaultNoteService extends NoteService {

    def createNote(noteGen: UUID => Note): Future[Note] = {
      log.info("Calling createNote service... ")
      val uuid = noteUUID.getUUID
      val note = noteGen(uuid)

      val result = redisClient.set(StringToBuf(uuid.toString), StringToBuf(note.asJson.noSpaces))

      result.flatMap(f => Future(note))
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
