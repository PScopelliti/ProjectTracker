package app.v1.service

import java.util.UUID

trait UUIDRandom extends UUIDService {

  def noteUUID: RandomNoteUUID = new RandomNoteUUID

  class RandomNoteUUID extends NoteUUID {
    def getUUID: UUID = UUID.randomUUID()
  }

}
