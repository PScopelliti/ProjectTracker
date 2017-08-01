package app.v1.service

import app.support.UUIDStub.getSomeUUID
import app.v1.model.Note
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ FlatSpec, Matchers }

trait UUIDTest extends ServiceDefault with UUIDComponent with MockFactory {
  val noteUUID: NoteUUID = stub[NoteUUID]
}

class ServiceDefaultTest extends FlatSpec with Matchers {

  "Create note method " should " insert create and return a Note" in new UUIDTest {

    override val noteService: DefaultNoteService = new DefaultNoteService

    // configure stubs
    (noteUUID.getUUID _).when().returns(getSomeUUID)

    noteService.getNotes should be(List(Note(getSomeUUID, "Note 1"), Note(getSomeUUID, "Note 2")))

    // Verify expectations met
    (noteUUID.getUUID _).verify().twice()
  }

}

