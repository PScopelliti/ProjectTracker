import sbt.Keys.{name, _}

object Settings {

  lazy val commonSettings = Seq(
    organization := "org.gscopelliti",
    scalaVersion := "2.11.9"
  )

  lazy val apiGatewaySettings = Seq(
    name := "Api Gateway",
    version := "1.0",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"), //, "-Xmx2G"),
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

  lazy val noteServiceSettings = Seq(
    name := "Note Service",
    version := "1.0",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"), //, "-Xmx2G"),
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )
}
