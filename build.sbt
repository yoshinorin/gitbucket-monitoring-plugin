val Name = "gitbucket-monitorting-plugin"
val Organization = "net.yoshinorin"
val Version = "3.1.0"

lazy val root = project in file(".")

name := Name
organization := Organization
version := Version
scalaVersion := "2.12.6"
gitbucketVersion := "4.25.0"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

scalafmtOnCompile := true