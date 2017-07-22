name := "FinchTest"

version := "1.0"

scalaVersion := "2.11.9"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.14.0",
  "com.github.finagle" %% "finch-circe" % "0.14.0",
  "io.circe" %% "circe-generic" % "0.7.0",

  "com.twitter" %% "twitter-server" % "1.30.0",

  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.1",

  "com.typesafe" % "config" % "1.3.0"
)
