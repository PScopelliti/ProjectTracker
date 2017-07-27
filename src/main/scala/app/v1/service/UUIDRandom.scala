package app.v1.service

import java.util.UUID

trait UUIDRandom extends UUIDComponent {

  def noteUUID = new RandomNoteUUID

  class RandomNoteUUID extends NoteUUID {
    def getUUID: UUID = UUID.randomUUID()
  }

}
