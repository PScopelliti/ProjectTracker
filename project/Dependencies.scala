import sbt._

object Dependencies {

  lazy val finagleVersion = "17.10.0"
  lazy val finchVersion = "0.15.1"
  lazy val circeVersion = "0.8.0"
  lazy val twitterServerVersion = "17.10.0"
  lazy val typesafeConfigVersion = "1.3.0"
  lazy val logbackVersion = "1.2.3"
  lazy val scalatestVersion = "3.0.4"
  lazy val scalaMockVersion = "3.6.0"
  lazy val cassandraVersion = "3.11.1"
  lazy val cassandraDriver = "3.3.0"
  lazy val cassandraUnitVersion = "3.3.0.2"
  lazy val phantomVersion = "2.15.5"

  val noteServiceDependencies = Seq(

    "com.github.finagle" %% "finch-core" % finchVersion,
    "com.github.finagle" %% "finch-circe" % finchVersion,

    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,

    "com.twitter" %% "twitter-server" % twitterServerVersion exclude("com.twitter", "libthrift"),
    "com.twitter" %% "finagle-stats" % finagleVersion,

    "ch.qos.logback" % "logback-classic" % logbackVersion,

    "com.typesafe" % "config" % typesafeConfigVersion,

    "com.outworkers" %% "phantom-dsl" % phantomVersion,
    "com.outworkers" %% "phantom-finagle" % phantomVersion,

    "org.cassandraunit" % "cassandra-unit" % cassandraUnitVersion % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion % "test",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"

  )

  val apiGatewayDependencies = Seq(

    "com.github.finagle" %% "finch-core" % finchVersion,
    "com.github.finagle" %% "finch-circe" % finchVersion,
    "com.github.finagle" %% "finch-oauth2" % finchVersion,

    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,

    "com.twitter" %% "twitter-server" % twitterServerVersion,
    "com.twitter" %% "finagle-stats" % finagleVersion,

    //Cassandra DB
    "org.apache.cassandra" % "cassandra-all" % cassandraVersion,
    "com.datastax.cassandra" % "cassandra-driver-core" % cassandraDriver,

    "com.typesafe" % "config" % typesafeConfigVersion,

    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion % "test",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"

  )
}