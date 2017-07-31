package app.v1.service

import java.util.UUID

trait UUIDComponent {

  val noteUUID: NoteUUID

  trait NoteUUID {
    def getUUID: UUID
  }

}
