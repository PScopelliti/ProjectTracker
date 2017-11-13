package app.v1.service

import app.v1.model.Note
import com.datastax.driver.core.{Cluster, Session}
import com.twitter.util.{Await, Future}
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}

class CassandraNoteServiceRepositoryTest
  extends FlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {


  private var cluster: Cluster = _
  private implicit var session: Session = _
  private var sut: CassandraNoteServiceRepository = _

  override protected def beforeAll(): Unit = {
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()
    cluster = Cluster.builder().addContactPoints("127.0.0.1")
      .withPort(9142)
      .build()
    session = cluster.connect()

    val dataSet: ClassPathCQLDataSet = new ClassPathCQLDataSet("ddl_v2.cql")
    val loader: CQLDataLoader = new CQLDataLoader(session)
    loader.load(dataSet)
  }

  override protected def beforeEach(): Unit = {
    //    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
    sut = new CassandraNoteServiceRepository
  }

  it should "do something " in {

    val result: Future[List[Note]] = sut.getAllNote()


    Await.result(result)
  }

}

