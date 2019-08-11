val Name = "gitbucket-monitorting-plugin"
val Organization = "net.yoshinorin"
val Version = "3.2.0"

lazy val root = project in file(".")

name := Name
organization := Organization
version := Version
scalaVersion := "2.13.0"
gitbucketVersion := "4.32.0"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

scalafmtOnCompile := true