package app

import app.config.ConfigurationLoader
import app.config.application.ApplicationProperty
import app.db.CassandraDatabaseProvider
import app.module.ServerModule
import app.v1.Api
import app.v1.api.NoteApi
import app.v1.handler.ErrorHandler
import app.v1.service.{NoteService, UUIDRandom}
import com.twitter.server._

object Main
  extends TwitterServer
    with ServerModule
    with ApplicationProperty
    with ConfigurationLoader
    with Api
    with NoteApi
    with UUIDRandom
    with NoteService
    with AdminHttpServer
    with Admin
    with CassandraDatabaseProvider
    with Lifecycle
    with ErrorHandler {

}
