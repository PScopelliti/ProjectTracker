package app.v1.api

import java.nio.charset.StandardCharsets
import java.util.UUID

import app.support.NoteStub.generateNote
import app.support.UUIDStub.getSomeUUID
import app.v1.model.Note
import app.v1.service.ServiceComponent
import com.twitter.finagle.http.Status
import com.twitter.util.Future
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{ Application, EndpointResult, Input }
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, Matchers }

trait NoteApiMock extends NoteApi with ServiceComponent with MockFactory {
  val noteService: NoteService = stub[NoteService]
}

class NoteApiTest extends FlatSpec with Matchers {

  val basePath = "/api/v1/notes"

  "Delete endpoint " should " return 200 status code if was successfully executed " in new NoteApiMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.delete(basePath + "/" + getSomeUUID)

    // sut
    val result = noteApi.deleteNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)

    // Verify expectations met
    (noteService.deleteNote _).verify(getSomeUUID).once()
  }

  "GetNoteById endpoint " should " return a note " in new NoteApiMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get(basePath + "/" + getSomeUUID)

    // configure stubs
    (noteService.getNoteById _).when(*).returns(Future(generateNote(getSomeUUID, "Note 1")))

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(getSomeUUID, "Note 1"))

    // Verify expectations met
    (noteService.getNoteById _).verify(getSomeUUID).once()
  }

  "CreateNotes endpoint " should " create a note " in new NoteApiMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.post(basePath).withBody[Application.Json](generateNote(getSomeUUID, "Note 1"), Some(StandardCharsets.UTF_8))
    val mockedFunction = mockFunction[UUID, Note]

    // configure stubs
    (noteService.createNote _).when(*).returns(Future.value(generateNote(getSomeUUID, "Note 1")))

    // sut
    val result = noteApi.createNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Created)

    // Verify expectations met
    (noteService.createNote _).verify(mockedFunction).once()
  }

  "PatchNote endpoint " should " modify a note " in new NoteApiMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.patch(basePath + "/" + getSomeUUID).withBody[Application.Json](generateNote(getSomeUUID, "Note 1"), Some(StandardCharsets.UTF_8))

    // configure stubs
    (noteService.patchNote _).when(*, *).returns(generateNote(getSomeUUID, "Note 1"))

    // sut
    val result = noteApi.patchNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(getSomeUUID, "Note 1"))

    // Verify expectations met
    (noteService.patchNote _).verify(getSomeUUID, generateNote(getSomeUUID, "Note 1")).once()
  }

  "Not implemented endpoint " should " not return any note" in new NoteApiMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get("some_path")

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result should be(EndpointResult.Skipped)

    // Verify expectations met
    (noteService.patchNote _).verify(*, *).never()
    (noteService.getNoteById _).verify(*).never()
    (noteService.createNote _).verify(*).never()
    (noteService.deleteNote _).verify(*).never()
  }
}
