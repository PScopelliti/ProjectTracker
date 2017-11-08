package app.v1.api

import java.util.UUID

import app.filter.Errors.notFoundError
import app.v1.model.Note
import app.v1.service.UUIDService
import com.twitter.finagle.http.Status
import com.twitter.logging.Logger
import com.twitter.util.Future
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Endpoint, _}

trait NoteApi {

  self: UUIDService with RepositoryConfig =>

  private val basePath = "api" :: "v1" :: "notes"
  private val log = Logger.get(getClass)

  val noteApi: DefaultNoteApi = new DefaultNoteApi

  class DefaultNoteApi {

    def endpoints = (getNoteById :+: createNote :+: deleteNote :+: patchNote :+: getAllNotes)

    def getNoteById: Endpoint[Note] = get(basePath :: uuid) { uuid: UUID =>
      log.info("Calling getNoteById endpoint... ")
      noteServiceRepository.getNote(uuid).map {
        case Some(x) => Ok(x)
        case None => NotFound(notFoundError(s"No note for ID $uuid"))
      }
    }

    def createNote: Endpoint[Note] = post(basePath :: jsonBody[UUID => Note]) {
      (noteGen: UUID => Note) => {
        log.info("Calling createNote endpoint... ")
        val note: Note = noteGen(noteUUID.getUUID)
        noteServiceRepository.setNote(note.id, note).map(Created)
      }
    }

    def patchNote: Endpoint[Note] = patch(basePath :: uuid :: jsonBody[Note => Note]) {
      (id: UUID, pt: Note => Note) => {
        log.info("Calling patchNote endpoint... ")
        noteServiceRepository.getNote(id).flatMap {
          case Some(x) => noteServiceRepository.updateNote(id, pt(x))
          case None => Future.exception(new RuntimeException)
        }.map(Ok)
      }
    }

    def deleteNote: Endpoint[Unit] = delete(basePath :: uuid) { uuid: UUID =>
      log.info("Calling deleteNote endpoint... ")
      noteServiceRepository.deleteNote(uuid).map {
        case true => {
          log.info(s"Deleted Note with $uuid successfully")
          NoContent[Unit].withStatus(Status.Ok)
        }
        case false => {
          log.error(s"Deleted note $uuid failed")
          NoContent[Unit].withStatus(Status.NotFound)
        }
      }
    }

    def getAllNotes: Endpoint[List[Note]] = get(basePath) {
      log.info("Calling getAllNote endpoint")


      val r: Future[Output[List[Note]]] = noteServiceRepository.getAllNote().map(Ok)
      r
    }

  }

}