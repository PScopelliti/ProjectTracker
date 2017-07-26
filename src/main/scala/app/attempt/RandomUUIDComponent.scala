package app.attempt

import java.util.UUID

trait RandomUUIDComponent extends UUIDComponent {

  def noteUUID = new RandomNoteUUID

  class RandomNoteUUID extends NoteUUID {
    def getUUID: UUID = UUID.randomUUID()
  }

}
