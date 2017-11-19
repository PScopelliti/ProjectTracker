package app.v1.model

import java.util.{Date, UUID}

case class Note(id: UUID,
                created: Date,
                userid: UUID,
                content: String)
