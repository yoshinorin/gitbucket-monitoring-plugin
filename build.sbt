val Name = "gitbucket-monitorting-plugin"
val Organization = "net.yoshinorin"
val Version = "5.0.0"

lazy val root = project in file(".")

name := Name
organization := Organization
version := Version
scalaVersion := "2.13.1"
gitbucketVersion := "4.35.0"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

scalafmtOnCompile := true