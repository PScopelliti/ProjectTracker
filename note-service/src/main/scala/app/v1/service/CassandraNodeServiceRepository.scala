package app.v1.service

import java.util.UUID

import app.utils.FutureConverters.listenableFutureToFinagleFuture
import app.v1.model.Note
import com.datastax.driver.core.{ResultSet, Session}
import com.google.common.util.concurrent.{AsyncFunction, Futures, ListenableFuture}
import com.twitter.util
import com.twitter.util.Future

class CassandraNodeServiceRepository(implicit val session: ListenableFuture[Session])
  extends NoteServiceRepository {

  def getNote(uuid: UUID): Future[Option[Note]] = {

    val resultSet: ListenableFuture[ResultSet] = Futures.transformAsync(session, new AsyncFunction[Session, ResultSet]() {
      def apply(session: Session) = {
        session.executeAsync("select * from test_gs.note where userid = 487db389-55eb-436e-bf58-341047579f41")
      }
    })

    val resFuture: util.Future[ResultSet] = resultSet

    null
  }

  def setNote(uuid: UUID, note: Note): Future[Note] = {
    null
  }

  def deleteNote(uuid: UUID): Future[Boolean] = {
    null
  }

  def updateNote(uuid: UUID, note: Note): Future[Note] = {
    null
  }

  def getAllNote(): Future[List[Note]] = {
    null
  }

}
