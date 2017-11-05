package app.v1.api

import java.util.UUID

import app.v1.model.Note
import app.v1.service.UUIDService
import com.twitter.finagle.http.Status
import com.twitter.logging.Logger
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{ Endpoint, _ }

trait NoteApi {

  self: UUIDService with RepositoryConfig =>

  private val basePath = "api" :: "v1" :: "notes"
  private val log = Logger.get(getClass)

  val noteApi: DefaultNoteApi = new DefaultNoteApi

  class DefaultNoteApi {

    def endpoints = (getNoteById :+: createNote :+: deleteNote :+: updateNote :+: getAllNotes)

    def getNoteById: Endpoint[Option[Note]] = get(basePath :: uuid) { uuid: UUID =>
      log.info("Calling getNoteById endpoint... ")
      noteServiceRepository.getNote(uuid).map(Ok)
    }

    def createNote: Endpoint[Note] = post(basePath :: jsonBody[UUID => Note]) {
      (noteGen: UUID => Note) =>
        {
          log.info("Calling createNote endpoint... ")

          noteGen andThen { note =>
            noteServiceRepository.setNote(note.id, note).map(Created)
          } apply (noteUUID.getUUID)
        }
    }

    def updateNote: Endpoint[Option[Note]] = post(basePath :: jsonBody[UUID => Note]) {
      (noteGen: UUID => Note) =>
        {
          log.info("Calling updateNote endpoint... ")

          noteGen andThen { note =>
            noteServiceRepository.updateNote(note.id, note).map(Created)
          } apply (noteUUID.getUUID)
        }
    }

    def deleteNote: Endpoint[Unit] = delete(basePath :: uuid) { uuid: UUID =>
      log.info("Calling deleteNote endpoint... ")
      noteServiceRepository.deleteNote(uuid)
      NoContent[Unit].withStatus(Status.Ok)
    }

    def getAllNotes: Endpoint[List[Note]] = get(basePath) {
      log.info("Calling getAllNote endpoint")
      noteServiceRepository.getAllNote().map(Ok)
    }

  }

}

