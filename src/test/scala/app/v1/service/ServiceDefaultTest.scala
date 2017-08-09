package app.v1.service

import java.util.UUID

import app.config.ConfigurationLoader
import app.config.datastore.RedisDBProperty
import app.module.RedisClientModule
import app.support.NoteStub.generateNote
import app.support.UUIDStub.getSomeUUID
import app.v1.model.Note
import com.twitter.finagle.redis.Client
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, Matchers }

trait UUIDTest extends ServiceDefault with RedisClientModule with RedisDBProperty with ConfigurationLoader with UUIDComponent with MockFactory {
  override val noteUUID: NoteUUID = stub[NoteUUID]
  override val redisClient: Client = stub[Client]
}

class ServiceDefaultTest extends FlatSpec with Matchers {

  it should "Get notes method - return a list of notes" in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    // configure stubs
    (noteUUID.getUUID _).when().returns(getSomeUUID)

    //sut
    noteService.getNotes should be(List(generateNote(getSomeUUID, "Note 1"), generateNote(getSomeUUID, "Note 2")))

    // Verify expectations met
    (noteUUID.getUUID _).verify().twice()
  }

  it should "Create note method - create a new note and return it" in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    // configure stubs
    val mockUUIDGenerator = stubFunction[UUID, Note]

    mockUUIDGenerator.when(getSomeUUID).returns(generateNote(getSomeUUID, "Note 1"))
    (noteUUID.getUUID _).when().returns(getSomeUUID)

    //sut
    noteService.createNote(mockUUIDGenerator) should be(generateNote(getSomeUUID, "Note 1"))

    // Verify expectations met
    (noteUUID.getUUID _).verify().once()
    mockUUIDGenerator.verify(getSomeUUID).once()
  }

  it should "Get note by id - return selected note " in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    //sut
    noteService.getNoteById(getSomeUUID) should be(generateNote(getSomeUUID, "Note 1"))

    // Verify expectations met
    (noteUUID.getUUID _).verify().never()
  }

  it should "Patch note - patch selected note " in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    val note = generateNote(getSomeUUID, "Note 1")

    //sut
    noteService.patchNote(getSomeUUID, note) should be(note)

    // Verify expectations met
    (noteUUID.getUUID _).verify().never()
  }

  it should "Delete note - delete selected note " in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    val note = generateNote(getSomeUUID, "Note 1")

    //sut
    noteService.deleteNote(getSomeUUID) should be()

    // Verify expectations met
    (noteUUID.getUUID _).verify().never()
  }

}

