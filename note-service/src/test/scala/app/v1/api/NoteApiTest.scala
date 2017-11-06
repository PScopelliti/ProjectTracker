package app.v1.api

import java.util.UUID

import app.support.NoteStub.generateNote
import app.v1.service.{NoteServiceRepository, UUIDService}
import com.datastax.driver.core.Session
import com.google.common.util.concurrent.ListenableFuture
import com.twitter.finagle.http.Status
import com.twitter.util.Future
import io.finch.{EndpointResult, Input}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

trait CassandraMock extends NoteApi
  with UUIDService
  with RepositoryConfig
  with MockFactory {

  override implicit val session: ListenableFuture[Session] = mock[ListenableFuture[Session]]

  override implicit val noteServiceRepository: NoteServiceRepository = stub[NoteServiceRepository]

  override def noteUUID: NoteUUID = stub[NoteUUID]
}

class NoteApiTest extends FlatSpec with Matchers {

  val someUUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d")
  val basePath = "/api/v1/notes"

  behavior of "Delete endpoint "

  it should " return 200 status code if was successfully executed " in new CassandraMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.delete(basePath + "/" + someUUID)

    // Set expectations
    (noteServiceRepository.deleteNote _) when (someUUID) returns (Future(true))

    // sut
    val result = noteApi.deleteNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)

    //Verify expectations
    (noteServiceRepository.deleteNote _) verify (someUUID) once
  }

  it should " return 404 status code if was unsuccessfully executed " in new CassandraMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.delete(basePath + "/" + someUUID)

    // Set expectations
    (noteServiceRepository.deleteNote _) when (someUUID) returns (Future(false))

    // sut
    val result = noteApi.deleteNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.NotFound)

    //Verify expectations
    (noteServiceRepository.deleteNote _) verify (someUUID) once
  }

  behavior of "GetNoteById endpoint "

  it should " return a note " in new CassandraMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get(basePath + "/" + someUUID)

    // configure stubs
    (noteServiceRepository.getNote _).when(*).returns(Future(Some(generateNote(someUUID, "Note 1"))))

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(someUUID, "Note 1"))

    // Verify expectations met
    (noteServiceRepository.getNote _).verify(someUUID).once()
  }

  it should " fail if not doesn't exist " in new CassandraMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get(basePath + "/" + someUUID)

    // configure stubs
    (noteServiceRepository.getNote _).when(*).returns(Future(None))

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.NotFound)

    // Verify expectations met
    (noteServiceRepository.getNote _).verify(someUUID).once()
  }

  "Not implemented endpoint " should " not return any note" in new CassandraMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get("some_path")

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result should be(EndpointResult.Skipped)

    // Verify expectations met
    (noteServiceRepository.getNote _).verify(*).never()
    (noteServiceRepository.setNote _).verify(*, *).never()
    (noteServiceRepository.deleteNote _).verify(*).never()
  }
}
