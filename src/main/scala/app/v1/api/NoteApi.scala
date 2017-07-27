package app.v1.api

import java.util.UUID

import app.attempt.{ServiceComponent, ServiceDefault, UUIDRandom}
import app.v1.model.Note
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Endpoint, _}

trait NoteApi {

  self: ServiceComponent =>

  private val basePath = "api" :: "v1" :: "notes"

  //  def getNotes: Endpoint[List[Note]] = get(basePath) {
  //    Ok(List(Note(uuidProvider, "Note 1"), Note(uuidProvider, "Note 2")))
  //  }

  //  def getNoteById: Endpoint[Note] = get(basePath :: uuid) { id: UUID =>
  //    Ok(Note(id, "Note 1"))
  //  }

  def createNote: Endpoint[Note] = post(basePath :: jsonBody[UUID => Note]) {
    (f: UUID => Note) =>
      Created(noteService.createNote)
  }

  //  def deleteNote: Endpoint[Unit] = delete(basePath :: uuid) { id: UUID =>
  //    NoContent[Unit].withStatus(Status.Ok)
  //  }

  //  def patchNote: Endpoint[Note] = patch(basePath :: uuid :: jsonBody[Note]) { (id: UUID, pt: Note) =>
  //    Ok(pt)
  //  }

  //def noteApis = (getNotes :+: getNoteById :+: createNote :+: deleteNote :+: patchNote)
  def noteApis = (createNote)
}


object NoteApi extends NoteApi with ServiceDefault with UUIDRandom