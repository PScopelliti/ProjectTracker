package app.support

import java.util

import com.datastax.driver.core.Session.State
import com.datastax.driver.core._
import com.google.common.util.concurrent.ListenableFuture

class SessionProxy(val session: Session) extends Session {

  override def prepare(query: String): PreparedStatement = session.prepare(query)

  override def prepare(statement: RegularStatement): PreparedStatement = session.prepare(statement)

  override def init(): Session = session.init()

  override def prepareAsync(query: String): ListenableFuture[PreparedStatement] = session.prepareAsync(query)

  override def prepareAsync(statement: RegularStatement): ListenableFuture[PreparedStatement] = session.prepareAsync(statement)

  override def getLoggedKeyspace: String = session.getLoggedKeyspace

  override def getCluster: Cluster = session.getCluster

  override def execute(query: String): ResultSet = session.execute(query)

  override def execute(query: String, values: AnyRef*): ResultSet = session.execute(query, values)

  override def execute(query: String, values: util.Map[String, AnyRef]): ResultSet = session.execute(query, values)

  override def execute(statement: Statement): ResultSet = session.execute(statement)

  override def initAsync(): ListenableFuture[Session] = session.initAsync()

  override def getState: State = session.getState

  override def isClosed: Boolean = session.isClosed

  override def closeAsync(): CloseFuture = session.closeAsync()

  // This is executed as sync for testing purposes.
  override def executeAsync(query: String): ResultSetFuture = new ResultSetFutureStub(session.execute(query))

  // This is executed as sync for testing purposes.
  override def executeAsync(query: String, values: AnyRef*): ResultSetFuture = new ResultSetFutureStub(session.execute(query, values))

  // This is executed as sync for testing purposes.
  override def executeAsync(query: String, values: util.Map[String, AnyRef]): ResultSetFuture = new ResultSetFutureStub(session.execute(query, values))

  // This is executed as sync for testing purposes.
  override def executeAsync(statement: Statement): ResultSetFuture = new ResultSetFutureStub(session.execute(statement))

  override def close(): Unit = session.close()
}
