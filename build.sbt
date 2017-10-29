import Dependencies._
import Settings._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbt.Keys.{exportJars, target}
import sbt.file
import sbtassembly.AssemblyPlugin.autoImport.assemblyJarName

import scalariform.formatter.preferences._

val preferences =
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)

val meta = """META.INF(.)*""".r

lazy val `api-gateway` = (project in file("api-gateway")).
  settings(commonSettings ++ apiGatewaySettings).
  settings(libraryDependencies ++= apiGatewayDependencies)

lazy val `note-service` = (project in file("note-service")).
  settings(commonSettings ++ noteServiceSettings).
  settings(libraryDependencies ++= noteServiceDependencies).
  settings(SbtScalariform.scalariformSettings ++ Seq(preferences)).
  settings(Seq(
    // Assembly
    mainClass in assembly := Some("app.Main"),
    target in assembly := file("target"),
    assemblyJarName in assembly := s"${name.value}_${version.value}.jar",
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
      case "BUILD" => MergeStrategy.discard
      case meta(_) => MergeStrategy.last
      case other => MergeStrategy.defaultMergeStrategy(other)
    },
    exportJars := true
  ))

lazy val root = (project in file(".")).
  aggregate(`api-gateway`, `note-service`)