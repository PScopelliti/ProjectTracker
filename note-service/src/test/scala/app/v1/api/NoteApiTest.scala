package app.v1.api

import java.util.UUID.fromString

import app.db.NoteDatabase
import app.support.EmbeddedCassandraConnector
import app.v1.model.Note
import app.v1.service.{ NoteService, UUIDService }
import com.twitter.finagle.http.Status
import io.circe.generic.auto._
import io.finch.Input
import io.finch.circe._
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.joda.time.DateTime
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
  val someNoteUUID = fromString("4a5d0831-4630-4e82-b3bb-80fe8a7dc9bd")
  val someUserUUID = fromString("05cd3079-a9ea-4cff-ba22-b4211a95d1be")

  behavior of "getNoteById endpoint"

  it should " return 200 status code if was successfully executed " in new CassandraMock {

    loadData(new ClassPathCQLDataSet("insert_notes.cql"))

    override val noteApi: DefaultNoteApi = new DefaultNoteApi

    val input = Input.get(basePath + "/" + someNoteUUID)

    // sut
    val result = noteApi.getNoteById(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
    result.awaitOutputUnsafe().map(_.value).get.id should be(someNoteUUID)
    result.awaitOutputUnsafe().map(_.value).get.userid should be(fromString("05cd3079-a9ea-4cff-ba22-b4211a95d1be"))
    result.awaitOutputUnsafe().map(_.value).get.created.toString should be("2016-12-10T19:06:21.000Z")
    result.awaitOutputUnsafe().map(_.value).get.content should be("some content 2")
  }

  behavior of "createNote endpoint"

  it should " return 201 if a note has been created succesfully " in new CassandraMock {

    val someNote = Note(someNoteUUID, someUserUUID, DateTime.parse("2012-08-16T07:22:05Z"), "some content 2")

    import Note._

    val input = Input.post(basePath).withBody(someNote)

    // configure stubs
    (noteUUID.getUUID _).when().returns(someNoteUUID)

    // sut
    val result = noteApi.createNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Created)

    // Verify expectations met
    (noteUUID.getUUID _).verify()
  }

  behavior of "updateNote endpoint"

  it should " return 200 if a note content has been updated succesfully " in new CassandraMock {

    loadData(new ClassPathCQLDataSet("insert_notes.cql"))

    val someNote = Note(someNoteUUID, someUserUUID, DateTime.parse("2012-08-16T07:22:05Z"), "some new content 2")

    import Note._

    val input = Input.patch(basePath + "/" + someNoteUUID).withBody(someNote)

    // sut
    val result = noteApi.patchNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.Ok)
  }

  it should " return 404 if a note uuid doesn't exist" in new CassandraMock {

    val someNote = Note(someNoteUUID, someUserUUID, DateTime.parse("2012-08-16T07:22:05Z"), "some new content 2")

    import Note._

    val input = Input.patch(basePath + "/" + someNoteUUID).withBody(someNote)

    // sut
    val result = noteApi.patchNote(input)

    // Verify result
    result.awaitOutputUnsafe().map(_.status).get should be(Status.NotFound)

  }

}