import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

name := "FinchTest"

version := "1.0"

scalaVersion := "2.11.9"

val finchVersion = "0.14.0"
val circeVersion = "0.8.0"
val twitterServerVersion = "1.30.0"
val typesafeConfigVersion = "1.3.0"
val typesafeLoggingVersion = "3.7.0"
val logbackVersion = "1.2.3"
val scalatestVersion = "3.0.3"
val mockitoVersion = "1.9.5"

libraryDependencies ++= Seq(

  "com.github.finagle" %% "finch-core" % finchVersion,
  "com.github.finagle" %% "finch-circe" % finchVersion,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  "com.twitter" %% "twitter-server" % twitterServerVersion,

  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % typesafeLoggingVersion,

  "com.typesafe" % "config" % typesafeConfigVersion,

  "org.mockito" % "mockito-all" % mockitoVersion % "test",
  "org.scalatest" %% "scalatest" % scalatestVersion % "test"

)

val preferences =
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)

SbtScalariform.scalariformSettings ++ Seq(preferences)