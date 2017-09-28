package app.v1.service

import java.util.UUID

import app.config.ConfigurationLoader
import app.config.datastore.RedisDBProperty
import app.module.RedisClientModule
import app.v1.model.Note
import com.twitter.finagle.redis.util.{BufToString, StringToBuf}
import com.twitter.logging.Logger
import com.twitter.util.Future
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

class RedisServiceRepository
  extends NoteServiceRepository
    with RedisClientModule
    with RedisDBProperty
    with ConfigurationLoader {

  private val log = Logger.get(getClass)

  def get(uuid: UUID): Future[Note] = {

    redisClient.get(StringToBuf(uuid.toString)).flatMap(_ match {
      case Some(value) => {
        val note = BufToString(value)

        decode[Note](note) match {
          case Left(msg) => {
            log.error("failed to parse Note from Redis")
            Future.exception(new Exception(msg))
          }
          case Right(result) => Future(result)
        }

      }
      case None => Future.exception(new Exception)
    })

  }

  def set(uuid: UUID, note: Note): Future[Unit] = {
    log.info("Successfully added note in redis key {}", uuid)
    redisClient.set(StringToBuf(uuid.toString), StringToBuf(note.asJson.noSpaces))
  }


  def delete(uuid: UUID): Future[Boolean] = {

    redisClient.dels(Seq(StringToBuf(uuid.toString))).flatMap(_ match {
      case v if v == 1 => Future(true)
      case _ => Future(false)
    })

  }

}
