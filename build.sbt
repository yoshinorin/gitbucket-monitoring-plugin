val Name = "gitbucket-monitorting-plugin"
val Organization = "net.yoshinorin"
val Version = "5.1.0"

compile / javacOptions ++= Seq("-target", "11", "-source", "11")

lazy val root = project in file(".")

name := Name
organization := Organization
version := Version
scalaVersion := "2.13.14"
gitbucketVersion := "4.37.2"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

scalafmtOnCompile := true
