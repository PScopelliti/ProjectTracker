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
  override val noteService: NoteService = stub[NoteService]
}

class NoteApiTest extends FlatSpec with Matchers {

  val basePath = "/api/v1/notes"

//  it should "Delete endpoint - return 200 status code if was successfully executed " in new NoteApiMock {
//
//    override val noteApi: DefaultNoteApi = new DefaultNoteApi
//
//    val input = Input.delete(basePath + "/" + getSomeUUID)
//
//    // sut
//    val result = noteApi.deleteNote(input)
//
//    // Verify result
//    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
//
//    // Verify expectations met
//    (noteService.deleteNote _).verify(getSomeUUID).once()
//  }
//
//  it should "GetNoteById endpoint - return a note " in new NoteApiMock {
//
//    override val noteApi: DefaultNoteApi = new DefaultNoteApi
//
//    val input = Input.get(basePath + "/" + getSomeUUID)
//
//    // configure stubs
//    (noteService.getNoteById _).when(*).returns(Future(generateNote(getSomeUUID, "Note 1")))
//
//    // sut
//    val result = noteApi.getNoteById(input)
//
//    // Verify result
//    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
//    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(getSomeUUID, "Note 1"))
//
//    // Verify expectations met
//    (noteService.getNoteById _).verify(getSomeUUID).once()
//  }

//  it should "GetNotes endpoint - return a list of notes " in new NoteApiMock {
//
//    override val noteApi: DefaultNoteApi = new DefaultNoteApi
//
//    val input = Input.get(basePath)
//
//    // configure stubs
//    (noteService.getNotes _).when().returns(Future((List(generateNote(getSomeUUID, "Note 1"), generateNote(getSomeUUID, "Note 2")))))
//
//    // sut
//    val result = noteApi.getNotes(input)
//
//    // Verify result
//    //result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
//   // result.awaitOutputUnsafe().map(_.value).get should be(List(generateNote(getSomeUUID, "Note 1"), generateNote(getSomeUUID, "Note 2")))
//
//    // Verify expectations met
//    (noteService.getNotes _).verify().once()
//  }


  it should "GetNotes endpoint - return a list of notes " in new NoteApiMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    //val input = Input.post(basePath).withBody[Application.Json](generateNote(getSomeUUID, "Note 1"), Some(StandardCharsets.UTF_8))
    val input = Input.get(basePath)

    // configure stubs
    (noteService.getNotes _).when().returns(Future((List(generateNote(getSomeUUID, "Note 1"), generateNote(getSomeUUID, "Note 2")))))

    // sut
    val result = noteApi.getNotes(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(List(generateNote(getSomeUUID, "Note 1"), generateNote(getSomeUUID, "Note 2")))

    // Verify expectations met
    (noteService.getNotes _).verify().twice()
  }
//
//  it should "PatchNote endpoint - modify a note " in new NoteApiMock {
//
//    override val noteApi: DefaultNoteApi = new DefaultNoteApi
//
//    val input = Input.patch(basePath + "/" + getSomeUUID).withBody[Application.Json](generateNote(getSomeUUID, "Note 1"), Some(StandardCharsets.UTF_8))
//
//    // configure stubs
//    (noteService.patchNote _).when(*, *).returns(generateNote(getSomeUUID, "Note 1"))
//
//    // sut
//    val result = noteApi.patchNote(input)
//
//    // Verify result
//    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
//    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(getSomeUUID, "Note 1"))
//
//    // Verify expectations met
//    (noteService.patchNote _).verify(getSomeUUID, generateNote(getSomeUUID, "Note 1")).once()
//  }
//
//  it should "Not implemented endpoint - not return any note" in new NoteApiMock {
//
//    override val noteApi: DefaultNoteApi = new DefaultNoteApi
//
//    val input = Input.get("some_path")
//
//    // sut
//    val result = noteApi.getNotes(input)
//
//    // Verify result
//    result should be(EndpointResult.Skipped)
//
//    // Verify expectations met
//    (noteService.patchNote _).verify(*, *).never()
//    (noteService.getNotes _).verify().never()
//    (noteService.getNoteById _).verify(*).never()
//    (noteService.createNote _).verify(*).never()
//    (noteService.deleteNote _).verify(*).never()
//  }
}
