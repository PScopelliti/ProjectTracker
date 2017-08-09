package app.module

import app.config.datastore.RedisDBProperty
import app.filter.RedisMetricsFilter
import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.protocol.{ Command, Reply }
import com.twitter.finagle.{ Redis, ServiceFactory }

trait RedisClientModule {

  self: RedisDBProperty =>

  private val redisServiceFactory: ServiceFactory[Command, Reply] = Redis.client
    .filtered(RedisMetricsFilter)
    .newClient(s"${dbProperties.url}:${dbProperties.port}", "redis-client")

  val redisClient: Client = Client(redisServiceFactory)

}
