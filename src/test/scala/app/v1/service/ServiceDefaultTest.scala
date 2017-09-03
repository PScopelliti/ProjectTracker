package app.v1.service

import java.util.UUID

import app.config.ConfigurationLoader
import app.config.datastore.RedisDBProperty
import app.support.NoteStub.generateNote
import app.support.UUIDStub.getSomeUUID
import app.v1.model.Note
import com.twitter.finagle.redis.Client
import com.twitter.util.Future
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

trait UUIDTest extends ServiceDefault with UUIDComponent with RedisClientModule with RedisDBProperty with ConfigurationLoader with MockFactory {
  val noteUUID: NoteUUID = stub[NoteUUID]
  override val redisClient: Client = stub[Client]
}

class ServiceDefaultTest extends FlatSpec with Matchers {

  "Create note method " should " create a new note and return it" in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    // configure stubs
    val mockUUIDGenerator = stubFunction[UUID, Note]

    mockUUIDGenerator.when(getSomeUUID).returns(generateNote(getSomeUUID, "Note 1"))
    (noteUUID.getUUID _).when().returns(getSomeUUID)
    (redisClient.set _).when(*,*).returns(Future.value(Unit))

    //sut
    noteService.createNote(mockUUIDGenerator) should be(generateNote(getSomeUUID, "Note 1"))

    // Verify expectations met
    (noteUUID.getUUID _).verify().once()
    mockUUIDGenerator.verify(getSomeUUID).once()

  }

  "Get note by id " should " return selected note " in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    //sut
    noteService.getNoteById(getSomeUUID) should be(generateNote(getSomeUUID, "Note 1"))

    // Verify expectations met
    (noteUUID.getUUID _).verify().never()
  }

  "Patch note " should " patch selected note " in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    val note = generateNote(getSomeUUID, "Note 1")

    //sut
    noteService.patchNote(getSomeUUID, note) should be(note)

    // Verify expectations met
    (noteUUID.getUUID _).verify().never()
  }

  "Delete note " should " delete selected note " in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    val note = generateNote(getSomeUUID, "Note 1")

    //sut
    noteService.deleteNote(getSomeUUID) should be()

    // Verify expectations met
    (noteUUID.getUUID _).verify().never()
  }

}

