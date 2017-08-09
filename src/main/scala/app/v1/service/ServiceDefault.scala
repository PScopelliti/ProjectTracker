package app.v1.service

import java.lang.{ Boolean => JBoolean, Long => JLong }
import java.util.UUID

import app.module.RedisClientModule
import app.v1.model.Note
import com.twitter.finagle.redis.util.{ BufToString, StringToBuf }
import com.twitter.logging.Logger
import com.twitter.util.Future
import io.circe.generic.auto._
import io.circe.parser._
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

      result.flatMap(_ => Future(note))
    }

    def getNotes: Future[List[Note]] = {
      log.info("Calling getNotes service... ")
      Future(List(Note(noteUUID.getUUID, "Note 1"), Note(noteUUID.getUUID, "Note 2")))
    }

    def getNoteById(uuid: UUID): Future[Note] = {
      log.info("Calling getNoteById service... ")
      Note(uuid, "Note 1")

      redisClient.get(StringToBuf(uuid.toString)).flatMap(_ match {
        case Some(value) => {
          val note = BufToString(value)

          val r = decode[Note](note)

          Future(r.right.get)
        }
        case None => Future.exception(new Exception)
      })
    }

    def deleteNote(uuid: UUID): Future[Boolean] = {
      log.info("Calling deleteNote service... ")

      redisClient.dels(Seq(StringToBuf(uuid.toString))).flatMap(_ match {
        case v if v == 1 => Future(true)
        case _           => Future(false)
      })

    }

    def patchNote(uuid: UUID, note: Note): Note = {
      log.info("Calling patchNote service... ")

      note
    }
  }

}
