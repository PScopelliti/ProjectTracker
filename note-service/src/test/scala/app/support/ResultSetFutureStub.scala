package app.support

import java.util.concurrent.{Executor, TimeUnit}

import com.datastax.driver.core.{ResultSet, ResultSetFuture}

class ResultSetFutureStub(val resultSet: ResultSet) extends ResultSetFuture {

  override def cancel(mayInterruptIfRunning: Boolean): Boolean = throw new UnsupportedOperationException

  override def getUninterruptibly: ResultSet = resultSet

  override def getUninterruptibly(timeout: Long, unit: TimeUnit): ResultSet = resultSet

  override def addListener(listener: Runnable, executor: Executor): Unit = throw new UnsupportedOperationException

  override def isCancelled: Boolean = throw new UnsupportedOperationException

  override def isDone: Boolean = throw new UnsupportedOperationException

  override def get(): ResultSet = resultSet

  override def get(timeout: Long, unit: TimeUnit): ResultSet = resultSet

}
