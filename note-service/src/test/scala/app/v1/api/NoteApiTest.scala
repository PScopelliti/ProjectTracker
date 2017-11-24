package app.v1.api

import java.util.UUID

import app.db.NoteDatabase
import app.support.EmbeddedCassandraConnector
import app.v1.service.{ NoteService, UUIDService }
import com.twitter.finagle.http.Status
import io.finch.Input
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.scalamock.scalatest.MockFactory

trait CassandraMock
  extends NoteApi
  with NoteService
  with UUIDService
  with MockFactory {

  override def database: NoteDatabase = new NoteDatabase(EmbeddedCassandraConnector.connector)

  override val noteUUID: NoteUUID = stub[NoteUUID]
}

class NoteApiTest extends EmbeddedCassandra {

  val basePath = "/api/v1/notes"

  behavior of "getNoteById endpoint "

  it should " return 200 status code if was successfully executed " in new CassandraMock {

    val someUUID = UUID.fromString("4a5d0831-4630-4e82-b3bb-80fe8a7dc9bd")

    loadData(new ClassPathCQLDataSet("insert_notes.cql"))

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get(basePath + "/" + someUUID)

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
  }

}