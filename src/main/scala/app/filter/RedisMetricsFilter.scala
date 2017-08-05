package app.filter

import app.metrics.AppStatsReceiver
import app.metrics.Metrics.clientMetrics
import com.twitter.finagle.redis.protocol.{Command, Reply}
import com.twitter.finagle.stats.Stat
import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future

abstract class SimpleRedisFilter[Command, Reply] extends Filter[Command, Reply, Command, Reply]

abstract class RedisMetricsFilter(statsReceiver: AppStatsReceiver) extends SimpleRedisFilter[Command, Reply] {

  private val stats = statsReceiver.scope("redis")

  final override def apply(request: Command, service: Service[Command, Reply]): Future[Reply] = {

    val label = s"${request.name.toString.toLowerCase}"

    Stat.timeFuture(stats.stat("stat-redis", label)) {
      val f = service(request)
      stats.counter("count-redis", label).incr()
      f
    }
  }

}

object RedisMetricsFilter extends RedisMetricsFilter(clientMetrics)
