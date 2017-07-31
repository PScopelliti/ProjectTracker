package app.v1.service

import java.util.UUID

import app.v1.model.Note
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

trait UUIDTest extends UUIDComponent with ServiceDefault with MockitoSugar {
  val noteUUID: NoteUUID = mock[NoteUUID]
}

class ServiceDefaultTest extends FlatSpec with Matchers {

  "Create note method " should " insert create and return a Note" in new UUIDTest {

    val someUUUID = UUID.randomUUID()

    override val noteService: DefaultNoteService = new DefaultNoteService

    when(noteUUID.getUUID).thenReturn(someUUUID)

    noteService.getNotes should be(List(Note(someUUUID, "Note 1"), Note(someUUUID, "Note 2")))

    verify(noteUUID, times(2)).getUUID
  }

}

