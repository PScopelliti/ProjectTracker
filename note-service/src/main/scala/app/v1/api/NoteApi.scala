package app.v1.api

import java.util.UUID

import app.filter.Errors.notFoundError
import app.filter.NotFoundError
import app.v1.model.Note
import app.v1.service.{ NoteService, UUIDService }
import com.twitter.logging.Logger
import com.twitter.util.Future
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{ Endpoint, _ }

trait NoteApi {

  self: NoteService with UUIDService =>

  val noteApi: DefaultNoteApi = new DefaultNoteApi
  private val basePath = "api" :: "v1" :: "notes"
  private val log = Logger.get(getClass)

  class DefaultNoteApi {

    def endpoints = (getNoteById :+: createNote :+: patchNote :+: deleteNote :+: getAllNotes)

    def getNoteById: Endpoint[Note] = get(basePath :: uuid) { uuid: UUID =>
      log.info("Calling getNoteById endpoint... ")
      findById(uuid).map {
        case Some(x) => Ok(x)
        case None    => NotFound(notFoundError(s"No note for ID $uuid"))
      }
    }

    import Note._

    def createNote: Endpoint[Note] = post(basePath :: jsonBody[UUID => Note]) {
      (noteGen: UUID => Note) =>
        log.info("Calling createNote endpoint... ")
        val note: Note = noteGen(noteUUID.getUUID)
        store(note.id, note).flatMap(_ => Future(note)).map(Created)
    }

    def patchNote: Endpoint[Note] = patch(basePath :: uuid :: jsonBody[Note]) {
      (uuid: UUID, note: Note) =>
        log.info("Calling patchNote endpoint... ")
        findById(uuid).flatMap {
          case Some(_) => updateItem(uuid, note).flatMap(_ => Future(note)).map(Ok)
          case None    => Future(NotFoundError(s"Not found note with id $uuid")).map(NotFound)
        }
    }

    def deleteNote: Endpoint[Unit] = delete(basePath :: uuid) {
      uuid: UUID =>
        log.info("Calling deleteNote endpoint... ")
        findById(uuid).flatMap {
          case Some(_) => deleteItem(uuid).flatMap(_ => Future.Unit).map(Ok)
          case None    => Future(NotFoundError(s"Not found note with id $uuid")).map(NotFound)
        }
    }

    def getAllNotes: Endpoint[List[Note]] = get(basePath) {
      log.info("Calling getAllNote endpoint")
      getItems().map(Ok)
    }

  }

}