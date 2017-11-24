package app.v1.api

import com.datastax.driver.core.{ Cluster, Session }
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.CQLDataSet
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers }

trait EmbeddedCassandra extends FlatSpec with BeforeAndAfterAll with BeforeAndAfterEach with Matchers {

  var cluster: Cluster = _
  var session: Session = _

  private lazy val schema = new ClassPathCQLDataSet("notes_table.cql")

  override def beforeAll(): Unit = {
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()
    cluster = EmbeddedCassandraServerHelper.getCluster
    session = cluster.connect()
  }

  override def beforeEach(): Unit = {
    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
    loadData(schema)
  }

  override def afterAll(): Unit = {
    session.close()
    cluster.close()
  }

  def loadData(dataset: CQLDataSet): Unit = {
    val loader = new CQLDataLoader(session)
    loader.load(dataset)
  }

}
