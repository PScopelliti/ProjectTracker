package app.service

import java.util.UUID

trait UuidProviderService {

  def uuidProvider = UUID.randomUUID

}
