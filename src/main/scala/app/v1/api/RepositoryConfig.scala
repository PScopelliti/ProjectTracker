package app.v1.api

import app.module.RedisClientFactory
import app.v1.service.{ NoteServiceRepository, RedisNodeServiceRepository }
import com.twitter.finagle.redis.Client

trait RepositoryConfig {

  private implicit val client: Client = RedisClientFactory.redisClient
  implicit val noteServiceRepository: NoteServiceRepository = new RedisNodeServiceRepository()

}

