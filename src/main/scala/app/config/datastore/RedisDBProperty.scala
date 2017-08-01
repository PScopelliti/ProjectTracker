package app.config.datastore

import app.config.ConfigurationLoader

trait RedisDBProperty {

  /**
   * Contains the name of the dynamo db table with settings.
   *
   * @return tableName
   */
  def settingsTableName: String

  /**
   * Contains the key of the dynamo db table with settings.
   *
   * @return
   */
  def settingsTableKey: String
}

object RedisDBPropertyImpl extends RedisDBProperty with ConfigurationLoader {

  /**
   * @inheritdoc
   */
  override def settingsTableName: String = configuration.getString("dynamodb.table.settings.name")

  /**
   * @inheritdoc
   */
  override def settingsTableKey: String = configuration.getString("dynamodb.table.settings.key")
}