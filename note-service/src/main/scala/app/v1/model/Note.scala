package app.v1.model

import java.util.UUID

import cats.syntax.either._
import io.circe.{Decoder, Encoder}
import org.joda.time.DateTime


case class Note(id: UUID,
                userid: UUID,
                date: DateTime,
                content: String)

object Note {

  implicit val encodeInstant: Encoder[DateTime] = Encoder.encodeString.contramap[DateTime](_.toString)

  implicit val decodeInstant: Decoder[DateTime] = Decoder.decodeString.emap { str =>
    Either.catchNonFatal(DateTime.parse(str)).leftMap(t => "DateTime")
  }

}
