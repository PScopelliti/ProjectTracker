import sbt.Keys.{name, _}

object Settings {
  lazy val commonSettings = Seq(
    organization := "org.myorg",
    name := "FinchTest",
    version := "1.0",
    scalaVersion := "2.11.9"
  )
}
