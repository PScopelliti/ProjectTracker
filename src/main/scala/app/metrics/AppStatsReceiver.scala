package app.metrics

import com.twitter.finagle.stats._

/**
 * An interface for recording metrics.
 *
 * Wraps a `com.twitter.finagle.stats.StatsReceiver` to provide a more restricted interface than the Finagle version,
 * scoped only to what we need.
 *
 * Do not access this directly, instead use `Metrics` object to obtain an appropriate instance.
 */
trait AppStatsReceiver {
  def scope(namespaces: String*): AppStatsReceiver

  def counter(name: String*): Counter

  def stat(name: String*): Stat

  def addGauge(name: String*)(f: => Float): Gauge

  def provideGauge(name: String*)(f: => Float): Unit
}

private final class AppStatsReceiver_(finagleStats: StatsReceiver) extends AppStatsReceiver {
  override def scope(namespaces: String*): AppStatsReceiver = new AppStatsReceiver_(finagleStats.scope(namespaces: _*))

  override def counter(name: String*): Counter = finagleStats.counter(name: _*)

  override def stat(name: String*): Stat = finagleStats.stat(name: _*)

  override def addGauge(name: String*)(f: => Float): Gauge = finagleStats.addGauge(name: _*)(f)

  override def provideGauge(name: String*)(f: => Float): Unit = finagleStats.provideGauge(name: _*)(f)
}

object AppStatsReceiver {
  lazy val stats: AppStatsReceiver = new AppStatsReceiver_(DefaultStatsReceiver.get)
}
