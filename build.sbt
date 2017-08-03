import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

name := "FinchTest"

version := "1.0"

scalaVersion := "2.11.9"

lazy val finagleVersion = "6.45.0"
lazy val finchVersion = "0.15.1"
lazy val circeVersion = "0.8.0"
lazy val twitterServerVersion = "1.30.0"
lazy val typesafeConfigVersion = "1.3.0"
lazy val slf4jVersion = "1.7.25"
lazy val logbackVersion = "1.2.3"
lazy val scalatestVersion = "3.0.3"
lazy val scalaMockVersion = "3.6.0"

libraryDependencies ++= Seq(

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

val preferences =
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)

SbtScalariform.scalariformSettings ++ Seq(preferences)

// Assembly
mainClass in assembly := Some("app.Main")

target in assembly := file("target")

assemblyJarName in assembly := s"${name.value}_${version.value}.jar"

val meta = """META.INF(.)*""".r

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case "BUILD" => MergeStrategy.discard
  case meta(_) => MergeStrategy.last
  case other => MergeStrategy.defaultMergeStrategy(other)
}

exportJars := true