import sbt._

object Dependencies {

  lazy val finagleVersion = "17.10.0"
  lazy val finchVersion = "0.15.1"
  lazy val circeVersion = "0.8.0"
  lazy val twitterServerVersion = "17.10.0"
  lazy val typesafeConfigVersion = "1.3.0"
  lazy val slf4jVersion = "1.7.25"
  lazy val logbackVersion = "1.2.3"
  lazy val scalatestVersion = "3.0.4"
  lazy val scalaMockVersion = "3.6.0"

  val noteServiceDependencies = Seq(

    "com.github.finagle" %% "finch-core" % finchVersion,
    "com.github.finagle" %% "finch-circe" % finchVersion,

    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,

    "com.twitter" %% "twitter-server" % twitterServerVersion,
    "com.twitter" %% "finagle-stats" % finagleVersion,
    "com.twitter" %% "finagle-redis" % finagleVersion,

    "org.slf4j" % "slf4j-simple" % slf4jVersion,

    "com.typesafe" % "config" % typesafeConfigVersion,

    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion % "test",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"

  )

  val apiGatewayDependencies = Seq(

    "com.github.finagle" %% "finch-core" % finchVersion,
    "com.github.finagle" %% "finch-circe" % finchVersion,
    "com.github.finagle" % "finch-oauth2_2.11" % finchVersion,

    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,

    "com.twitter" %% "twitter-server" % twitterServerVersion,
    "com.twitter" %% "finagle-stats" % finagleVersion,
    "com.twitter" %% "finagle-redis" % finagleVersion,

    "org.slf4j" % "slf4j-simple" % slf4jVersion,

    "com.typesafe" % "config" % typesafeConfigVersion,

    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion % "test",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"

  )
}