package app.v1.api

import java.nio.charset.StandardCharsets
import java.util.UUID

import app.support.NoteStub.generateNote
import app.support.UUIDStub.getSomeUUID
import app.v1.model.Note
import app.v1.service.{ NoteServiceRepository, UUIDService }
import com.twitter.finagle.http.Status
import com.twitter.finagle.redis.Client
import com.twitter.util.Future
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{ Application, EndpointResult, Input }
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, Matchers }

trait RedisConfMock extends NoteApi
  with UUIDService
  with RepositoryConfig
  with MockFactory {

  private implicit val mockRedisClient: Client = stub[Client]
  override implicit val noteServiceRepository: NoteServiceRepository = stub[NoteServiceRepository]

  override val noteUUID: NoteUUID = stub[NoteUUID]
}

class NoteApiTest extends FlatSpec with Matchers {

  val basePath = "/api/v1/notes"

  "Delete endpoint " should " return 200 status code if was successfully executed " in new RedisConfMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.delete(basePath + "/" + getSomeUUID)

    // sut
    val result = noteApi.deleteNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)

    // Verify expectations met
    (noteServiceRepository.deleteItem _).verify(getSomeUUID).once()
  }

  "GetNoteById endpoint " should " return a note " in new RedisConfMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get(basePath + "/" + getSomeUUID)

    // configure stubs
    (noteServiceRepository.getItem _).when(*).returns(Future(Some(generateNote(getSomeUUID, "Note 1"))))

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get should be(generateNote(getSomeUUID, "Note 1"))

    // Verify expectations met
    (noteServiceRepository.getItem _).verify(getSomeUUID).once()
  }

  "Not implemented endpoint " should " not return any note" in new RedisConfMock {

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get("some_path")

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result should be(EndpointResult.Skipped)

    // Verify expectations met
    (noteServiceRepository.getItem _).verify(*).never()
    (noteServiceRepository.setItem _).verify(*, *).never()
    (noteServiceRepository.deleteItem _).verify(*).never()
  }
}
