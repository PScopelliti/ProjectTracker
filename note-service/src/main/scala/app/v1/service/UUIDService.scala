package app.v1.service

import java.util.UUID

trait UUIDService {

  def noteUUID: NoteUUID

  trait NoteUUID {
    def getUUID: UUID
  }

}