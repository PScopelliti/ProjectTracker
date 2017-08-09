package app.v1.api

import java.util.UUID

import app.v1.model.Note
import app.v1.service.ServiceComponent
import com.twitter.finagle.http.Status
import com.twitter.logging.Logger
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Endpoint, _}

trait NoteApi {

  self: ServiceComponent =>

  private val basePath = "api" :: "v1" :: "notes"
  private val log = Logger.get(getClass)

  val noteApi: DefaultNoteApi = new DefaultNoteApi

  class DefaultNoteApi {

    def endpoints = (getNotes :+: getNoteById :+: createNote :+: deleteNote :+: patchNote)

    def getNotes: Endpoint[List[Note]] = get(basePath) {
      //log.info("Calling getNotes endpoint... ")
      val result = noteService.getNotes.map(Ok)
      result
    }

    def getNoteById: Endpoint[Note] = get(basePath :: uuid) { uuid: UUID =>
      log.info("Calling getNoteById endpoint... ")
      noteService.getNoteById(uuid).map(Ok)
    }

    def createNote: Endpoint[Note] = get(basePath) {
        log.info("Calling createNote endpoint... ")
        val result = noteService.createNote(null)
        result.map(Created)
    }

    def deleteNote: Endpoint[Unit] = delete(basePath :: uuid) { uuid: UUID =>
      log.info("Calling deleteNote endpoint... ")
      noteService.deleteNote(uuid)
      NoContent[Unit].withStatus(Status.Ok)
    }

    def patchNote: Endpoint[Note] = patch(basePath :: uuid :: jsonBody[Note]) { (uuid: UUID, note: Note) =>
      log.info("Calling patchNote endpoint... ")
      noteService.patchNote(uuid, note)
      Ok(note)
    }

  }

}
