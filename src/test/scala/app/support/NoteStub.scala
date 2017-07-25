package app.support

import java.util.UUID

import app.v1.model.Note

object NoteStub {

  def generateNote(id: UUID, text: String) = Note(id, text)

}
