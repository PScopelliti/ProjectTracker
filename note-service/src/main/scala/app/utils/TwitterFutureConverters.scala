package app.utils

import com.google.common.util.concurrent.{ FutureCallback, Futures, ListenableFuture }
import com.twitter.{ util => twitter }

import scala.language.implicitConversions

object FutureConverters {

  implicit def listenableFutureToFinagleFuture[T](listenableFuture: ListenableFuture[T]): twitter.Future[T] = {
    val promise = twitter.Promise[T]()
    Futures.addCallback(listenableFuture, new FutureCallback[T] {
      def onFailure(error: Throwable): Unit = {
        promise.update(twitter.Throw(error))
      }

      def onSuccess(result: T): Unit = {
        promise.update(twitter.Return(result))
      }
    })
    promise
  }

}