package app.db

import com.datastax.driver.core.Session
import com.outworkers.phantom.connectors.KeySpace

trait RootConnector {

  implicit def space: KeySpace

  implicit def session: Session
}
