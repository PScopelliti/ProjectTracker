package app.support

import app.v1.model.Note

object NoteStub {

  def generateNote(id: String, text: String) = Note(id, text)

}
