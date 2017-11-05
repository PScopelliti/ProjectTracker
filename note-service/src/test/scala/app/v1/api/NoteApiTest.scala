package app.v1.api

import app.support.NoteStub.generateNote
import app.support.UUIDStub.getSomeUUID
import app.v1.service.{ NoteServiceRepository, UUIDService }
import com.datastax.driver.core.Session
import com.google.common.util.concurrent.ListenableFuture
import com.twitter.finagle.http.Status
import com.twitter.util.Future
import io.finch.{ EndpointResult, Input }
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, Matchers }

trait CassandraMock extends NoteApi
  with UUIDService
  with RepositoryConfig
  with MockFactory {

  override implicit lazy val session: ListenableFuture[Session] = mock[ListenableFuture[Session]]
  override implicit val noteServiceRepository: NoteServiceRepository = mock[NoteServiceRepository]

  override val noteUUID: NoteUUID = stub[NoteUUID]
}

class NoteApiTest extends FlatSpec with Matchers {

  val basePath = "/api/v1/notes"

  "Delete endpoint " should " return 200 status code if was successfully executed " in new CassandraMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.delete(basePath + "/" + getSomeUUID)

    // sut
    val result = noteApi.deleteNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)

    // Verify expectations met
    (noteServiceRepository.deleteNote _).verify(getSomeUUID).once()
  }

  "GetNoteById endpoint " should " return a note " in new CassandraMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get(basePath + "/" + getSomeUUID)

    // configure stubs
    (noteServiceRepository.getNote _).when(*).returns(Future(Some(generateNote(getSomeUUID, "Note 1"))))

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(getSomeUUID, "Note 1"))

    // Verify expectations met
    (noteServiceRepository.getNote _).verify(getSomeUUID).once()
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
