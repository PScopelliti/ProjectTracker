import Dependencies._
import Settings._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

commonSettings

libraryDependencies ++= dependencies

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