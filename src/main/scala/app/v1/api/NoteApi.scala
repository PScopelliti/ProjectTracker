package app.v1.api

import app.v1.model.Note
import com.twitter.finagle.http.Status
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{BadRequest, Endpoint, Ok, _}

trait NoteApi {

  private val basePath = "api" :: "v1" :: "notes"

  def getNotes: Endpoint[List[Note]] = get(basePath) {
    Ok(List(Note("1", "Note 1"), Note("2", "Note 2")))
  }

  def getNoteById: Endpoint[Note] = get(basePath :: string) { s: String =>
    if (s != "") Ok(Note("1", "Note 1"))
    else BadRequest(new IllegalArgumentException("empty string"))
  }

  def createNote: Endpoint[Note] = post(basePath :: jsonBody[Note]) {
    l: Note => Created(Note(l.id, l.text))
  }

  def deleteNote: Endpoint[Unit] = delete(basePath :: string) { s: String =>
    NoContent[Unit].withStatus(Status.Ok)
  }

  def patchNote: Endpoint[Note] = patch(basePath :: jsonBody[Note]) {
    l: Note => Ok(Note(l.id, l.text))
  }

  def noteApis = (getNotes :+: getNoteById :+: createNote :+: deleteNote :+: patchNote)
}


object NoteApi extends NoteApi