name := "fractus"

organization := "net.aethersanctum"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "11.0",
  "junit" % "junit" % "4.11" % "test",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test"
)

ScoverageSbtPlugin.instrumentSettings

publishArtifact in Test := false

parallelExecution in Test := false