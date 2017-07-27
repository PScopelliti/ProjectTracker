package app.attempt

import java.util.UUID

trait UUIDComponent {

  def noteUUID: NoteUUID

  trait NoteUUID {
    def getUUID: UUID
  }

}
