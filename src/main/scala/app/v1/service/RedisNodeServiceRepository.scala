package app.v1.service

import java.io.FileNotFoundException
import java.util.UUID

import app.v1.model.Note
import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.{BufToString, StringToBuf}
import com.twitter.logging.Logger
import com.twitter.util.{Future, Promise}
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

class RedisNodeServiceRepository(implicit val redisClient: Client) extends NoteServiceRepository {

  private val log = Logger.get(getClass)

  def getItem(uuid: UUID): Future[Option[Note]] = {

    redisClient.get(StringToBuf(uuid.toString)).flatMap(_ match {
      case Some(value) => {
        val note = BufToString(value)

        decode[Note](note) match {
          case Left(msg) => {
            log.error(s"Failed to parse Note from Redis: ${msg}")
            Future.exception(new Exception(msg))
          }
          case Right(result) => {
            log.info(s"Found result in redis : key - ${uuid}")
            Future(Some(result))
          }
        }

      }
      case None => {
        log.info(s"No value found in redis with key: ${uuid}")
        Future(None)
      }
    })

  }

  def setItem(uuid: UUID, note: Note): Future[Note] = {

    val returnVal = new Promise[Note]
    log.info(s"Set item inside store: ${note}")

    redisClient.set(StringToBuf(uuid.toString), StringToBuf(note.asJson.noSpaces))
      .onSuccess { _ =>
        log.info(s"Successfully added entry with key: ${uuid}")
        returnVal.setValue(Note(uuid, note.text))
      }
      .onFailure { _ =>
        log.info(s"Failed to insert entry with key: ${uuid}")
        returnVal.setException(new FileNotFoundException())
      }

    returnVal
  }

  def deleteItem(uuid: UUID): Future[Boolean] = {

    redisClient.dels(Seq(StringToBuf(uuid.toString))).flatMap(_ match {
      case v if v == 1 => {
        log.info(s"Successfully deleted note in redis key: ${uuid}")
        Future(true)
      }
      case _ => {
        log.error(s"Failed to delete note in redis with key: ${uuid}")
        Future(false)
      }
    })

  }

}
