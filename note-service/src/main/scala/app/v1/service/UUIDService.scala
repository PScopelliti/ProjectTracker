package app.v1.service

import java.util.UUID

trait UUIDService {

  val noteUUID: NoteUUID

  trait NoteUUID {
    def getUUID: UUID
  }

}
