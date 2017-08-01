package app.config.application

import app.config.ConfigurationLoader

trait ApplicationProperty {

  /**
   * Endpoints for fetching the channels, items and packages from reuters.
   *
   * @return
   */
  def reutersBaseUriEndpoint: String

  /**
   * Endpoint for fetching the authentication token for Reuters
   *
   * @return
   */
  def reutersAuthenticationUriEndpoint: String

  /**
   * The common path contained in the Api path
   *
   * @return
   */
  def reutersCommonPathUri: String

  /**
   * The application port
   *
   * @return
   */
  def reutersApplicationPort: String

  /**
   * The username used for authenticate against Reuters APIs
   *
   * @return
   */
  def reutersApplicationUsername: String

  /**
   * The password used for authenticate against Reuters APIs
   *
   * @return
   */
  def reutersApplicationPassword: String

}

object ApplicationPropertyImpl extends ApplicationProperty with ConfigurationLoader {

  /**
   * @inheritdoc
   */
  override def reutersBaseUriEndpoint: String = configuration.getString("app.uri.endpoint.base")

  /**
   * @inheritdoc
   */
  override def reutersAuthenticationUriEndpoint: String = configuration.getString("app.uri.endpoint.authentication")

  /**
   * @inheritdoc
   */
  override def reutersCommonPathUri: String = configuration.getString("app.uri.path.common")

  /**
   * @inheritdoc
   */
  override def reutersApplicationPort: String = configuration.getString("app.port")

  /**
   * @inheritdoc
   */
  override def reutersApplicationUsername: String = configuration.getString("app.username")

  /**
   * @inheritdoc
   */
  override def reutersApplicationPassword: String = configuration.getString("app.password")

}