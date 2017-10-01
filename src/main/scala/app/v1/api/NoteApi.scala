package app.v1.api

import java.util.UUID

import app.module.RedisClientFactory
import app.v1.model.Note
import app.v1.service.{ NoteServiceRepository, RedisNodeServiceRepository, UUIDService }
import com.twitter.finagle.http.Status
import com.twitter.finagle.redis.Client
import com.twitter.logging.Logger
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{ Endpoint, _ }

trait NoteApi {

  self: UUIDService =>

  private val basePath = "api" :: "v1" :: "notes"
  private val log = Logger.get(getClass)

  val noteApi: DefaultNoteApi = new DefaultNoteApi

  class DefaultNoteApi {

    def endpoints = (getNoteById :+: createNote :+: deleteNote :+: patchNote)

    def getNoteById: Endpoint[Option[Note]] = get(basePath :: uuid) { uuid: UUID =>
      log.info("Calling getNoteById endpoint... ")
      RedisConf.noteServiceRepository.getItem(uuid).map(Ok)
    }

    def createNote: Endpoint[Note] = post(basePath :: jsonBody[UUID => Note]) {
      (noteGen: UUID => Note) =>
        {
          log.info("Calling createNote endpoint... ")

          noteGen andThen { note =>
            RedisConf.noteServiceRepository.setItem(note.id, note).map(Created)
          } apply (noteUUID.getUUID)
        }
    }

    def deleteNote: Endpoint[Unit] = delete(basePath :: uuid) { uuid: UUID =>
      log.info("Calling deleteNote endpoint... ")
      RedisConf.noteServiceRepository.deleteItem(uuid)
      NoContent[Unit].withStatus(Status.Ok)
    }

    def patchNote: Endpoint[Note] = patch(basePath :: uuid :: jsonBody[Note]) { (uuid: UUID, note: Note) =>
      log.info("Calling patchNote endpoint... ")
      RedisConf.noteServiceRepository.setItem(note.id, note)
      Ok(note)
    }

  }

}

trait RedisConf {
  private implicit val redisClient: Client = RedisClientFactory.redisClient
  implicit val noteServiceRepository: NoteServiceRepository = new RedisNodeServiceRepository()
}

object RedisConf extends RedisConf
