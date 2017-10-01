package app.v1.service

import java.util.UUID

import app.v1.model.Note
import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.{ BufToString, StringToBuf }
import com.twitter.logging.Logger
import com.twitter.util.Future
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
            log.error("failed to parse Note from Redis")
            Future.exception(new Exception(msg))
          }
          case Right(result) => {
            log.info("Found result in redis : key {}", uuid)
            Future(Some(result))
          }
        }

      }
      case None => {
        log.info("No value found in redis with key {}", uuid)
        Future(None)
      }
    })

  }

  def setItem(uuid: UUID, note: Note): Future[Note] = {
    log.info("Successfully added note in redis key {}", uuid)
    redisClient.set(StringToBuf(uuid.toString), StringToBuf(note.asJson.noSpaces))
    Future(Note(uuid, note.text))
  }

  def deleteItem(uuid: UUID): Future[Boolean] = {

    redisClient.dels(Seq(StringToBuf(uuid.toString))).flatMap(_ match {
      case v if v == 1 => {
        log.info("Successfully deleted note in redis key {}", uuid)
        Future(true)
      }
      case _ => {
        log.error("Failed to delete note in redis with key {}", uuid)
        Future(false)
      }
    })

  }

}
