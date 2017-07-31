package app.v1.api

import java.util.UUID

import app.v1.model.Note
import app.v1.service.ServiceComponent
import com.twitter.finagle.http.Status
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{ Endpoint, _ }

trait NoteApi {

  self: ServiceComponent =>

  private val basePath = "api" :: "v1" :: "notes"

  val noteApi: DefaultNoteApi = new DefaultNoteApi

  class DefaultNoteApi {

    def endpoints = (getNotes :+: getNoteById :+: createNote :+: deleteNote :+: patchNote)

    def getNotes: Endpoint[List[Note]] = get(basePath) {
      Ok(noteService.getNotes)
    }

    def getNoteById: Endpoint[Note] = get(basePath :: uuid) { uuid: UUID =>
      Ok(noteService.getNoteById(uuid))
    }

    def createNote: Endpoint[Note] = post(basePath :: jsonBody[UUID => Note]) {
      (noteGen: UUID => Note) =>
        {
          Created(noteService.createNote(noteGen))
        }
    }

    def deleteNote: Endpoint[Unit] = delete(basePath :: uuid) { uuid: UUID =>
      noteService.deleteNote(uuid)
      NoContent[Unit].withStatus(Status.Ok)
    }

    def patchNote: Endpoint[Note] = patch(basePath :: uuid :: jsonBody[Note]) { (uuid: UUID, note: Note) =>
      noteService.patchNote(uuid, note)
      Ok(note)
    }

  }

}
