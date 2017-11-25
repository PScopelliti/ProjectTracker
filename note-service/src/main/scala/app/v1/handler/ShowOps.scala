package app.v1.handler

import cats.Show
import com.twitter.io.Buf


trait ShowOps {
  final val showBuf: Show[Buf] = new Show[Buf] {
    override def show(b: Buf): String = BufOps.bufToString(b)
  }

  final val showThrowable: Show[Throwable] = new Show[Throwable] {
    override def show(t: Throwable): String = t.getMessage
  }
}

object ShowOps extends ShowOps

