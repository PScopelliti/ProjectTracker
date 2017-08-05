package app.module

import app.config.datastore.RedisDBProperty
import app.filter.RedisMetricsFilter
import com.twitter.finagle.Redis


trait RedisClientModule {

  self: RedisDBProperty =>

  val redisClient = Redis.client
    .filtered(RedisMetricsFilter)
    .newClient(s"${dbProperties.url}:${dbProperties.port}", "redis-cient")

}
