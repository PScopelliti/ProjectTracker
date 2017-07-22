package app.v1.api

import com.twitter.finagle.http.Status
import io.finch.Input
import org.scalatest.FlatSpec

class NoteApiTest extends FlatSpec {

  val basePath = "/api/v1/notes/"

  "Delete endpoint" should "return 200 status code if was successfully" in {

    import app.v1.api.NoteApi._

    val input = Input.delete(basePath + "some_note_id")

    val result = deleteNote(input)

    assert(result.awaitOutputUnsafe().map(_.status) === Some(Status.Ok))
  }



}
