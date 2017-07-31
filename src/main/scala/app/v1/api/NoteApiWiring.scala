package app.v1.api

import app.v1.service.{ ServiceDefault, UUIDRandom }

object NoteApiWiring extends NoteApi with ServiceDefault with UUIDRandom