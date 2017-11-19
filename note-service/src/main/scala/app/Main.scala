package app

import app.config.ConfigurationLoader
import app.config.application.ApplicationProperty
import app.db.CassandraDatabaseProvider
import app.module.ServerModule
import app.v1.Api
import app.v1.api.{NoteApi, RepositoryConfig}
import app.v1.handler.ErrorHandler
import app.v1.service.{NoteService, UUIDRandom}
import com.twitter.server._
import io.circe.generic.auto._
import io.finch.circe._

object Main
  extends TwitterServer
    with ServerModule
    with ApplicationProperty
    with ConfigurationLoader
    with Api
    with NoteApi
  with RepositoryConfig
    with NoteService
    with UUIDRandom
    with AdminHttpServer
    with CassandraDatabaseProvider
    with Admin
    with Lifecycle
    with ErrorHandler {

}
