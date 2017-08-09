package app

import app.config.ConfigurationLoader
import app.config.application.ApplicationProperty
import app.config.datastore.RedisDBProperty
import app.module.{ RedisClientModule, ServerModule }
import app.v1.Api
import app.v1.api.NoteApi
import app.v1.handler.ErrorHandler
import app.v1.service.{ ServiceDefault, UUIDRandom }
import com.twitter.server._

object Main
  extends TwitterServer
  with ServerModule
  with ApplicationProperty
  with ConfigurationLoader
  with Api
  with NoteApi
  with AdminHttpServer
  with RedisDBProperty
  with Admin
  with Lifecycle
  with RedisClientModule
  with ServiceDefault
  with UUIDRandom
  with ErrorHandler {

}
