package app.v1.api

import java.nio.charset.StandardCharsets
import java.util.UUID

import app.support.NoteStub.generateNote
import app.v1.service.ServiceComponent
import com.twitter.finagle.http.Status
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{ Application, EndpointResult, Input }
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{ FlatSpec, Matchers }

trait NoteApiMock extends NoteApi with ServiceComponent with MockitoSugar {
  val noteService: NoteService = mock[NoteService]
}

class NoteApiTest extends FlatSpec with Matchers {

  val basePath = "/api/v1/notes"
  val someUUID = UUID.randomUUID()

  "Delete endpoint " should " return 200 status code if was successfully executed " in new NoteApiMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.delete(basePath + "/" + someUUID)

    val result = noteApi.deleteNote(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
  }

  "GetNoteById endpoint " should " return a note " in new NoteApiMock {

    val input = Input.get(basePath + "/" + someUUID)

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    when(noteService.getNoteById(any(classOf[UUID]))).thenReturn(generateNote(someUUID, "Note 1"))

    val result = noteApi.getNoteById(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(someUUID, "Note 1"))
  }

  "GetNotes endpoint " should " return a list of notes " in new NoteApiMock {

    val someUUUID = UUID.randomUUID()

    val input = Input.get(basePath)

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    when(noteService.getNotes).thenReturn(List(generateNote(someUUID, "Note 1"), generateNote(someUUID, "Note 2")))

    val result = noteApi.getNotes(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(List(generateNote(someUUID, "Note 1"), generateNote(someUUID, "Note 2")))
  }

  "CreateNotes endpoint " should " create a note " in new NoteApiMock {

    val input = Input.post(basePath).withBody[Application.Json](generateNote(someUUID, "Note 1"), Some(StandardCharsets.UTF_8))

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val result = noteApi.createNote(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Created)
  }

  "PatchNote endpoint " should " modify a note " in new NoteApiMock {

    val input = Input.patch(basePath + "/" + someUUID).withBody[Application.Json](generateNote(someUUID, "Note 1"), Some(StandardCharsets.UTF_8))

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val result = noteApi.patchNote(input)

    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(someUUID, "Note 1"))
  }

  "Not implemented endpoint " should " not return any note" in new NoteApiMock {

    val input = Input.get("some_path")

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val result = noteApi.getNotes(input)

    result should be(EndpointResult.Skipped)
  }
}
