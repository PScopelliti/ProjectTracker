package app.v1.service

import java.util.UUID

import app.utils.FutureConverters.listenableFutureToFinagleFuture
import app.v1.model.Note
import com.datastax.driver.core.{ResultSet, ResultSetFuture, Session}
import com.twitter.util
import com.twitter.util.{Await, Future}

class CassandraNoteServiceRepository(implicit val session: Session)
  extends NoteServiceRepository {

  def findById(uuid: UUID): Future[Option[Note]] = {

    val resultSet: ResultSetFuture = session.executeAsync("select * from test_gs.note where userid = 487db389-55eb-436e-bf58-341047579f41")

    val resFuture: util.Future[ResultSet] = resultSet

    null
  }



}
