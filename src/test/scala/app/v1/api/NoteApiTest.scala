package app.v1.api

import java.nio.charset.StandardCharsets

import app.support.NoteStub.generateNote
import com.twitter.finagle.http.Status
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Application, EndpointResult, Input}
import org.scalatest.{FlatSpec, Matchers}

class NoteApiTest extends FlatSpec with Matchers {

  val basePath = "/api/v1/notes"

  import app.v1.api.NoteApi._

  "Delete endpoint " should " return 200 status code if was successfully executed " in {

    val input = Input.delete(basePath + "/some_note_id")

    val result = deleteNote(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
  }

  "GetNoteById endpoint " should " return a note " in {

    val input = Input.get(basePath + "/some_note_id")

    val result = getNoteById(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote("1", "Note 1"))
  }

  "GetNotes endpoint " should " return a list of notes " in {

    val input = Input.get(basePath)

    val result = getNotes(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(List(generateNote("1", "Note 1"), generateNote("2", "Note 2")))
  }

  "CreateNotes endpoint " should " create a note " in {

    val input = Input.post(basePath).withBody[Application.Json](generateNote("1", "Note 1"), Some(StandardCharsets.UTF_8))

    val result = createNote(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Created)
  }

  "PatchNote endpoint " should " modify a note " in {

    val input = Input.patch(basePath).withBody[Application.Json](generateNote("1", "Note 1"), Some(StandardCharsets.UTF_8))

    val result = patchNote(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote("1", "Note 1"))
  }

  "Not implemented endpoint " should " not return any note" in {
    val input = Input.get("some_path")

    val result = getNotes(input)

    result should be(EndpointResult.Skipped)
  }
}
