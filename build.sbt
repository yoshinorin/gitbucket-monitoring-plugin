val Name = "gitbucket-monitorting-plugin"
val Organization = "com.github.YoshinoriN"
val Version = "2.0.0"

lazy val root = project in file(".")

name := Name
organization := Organization
version := Version
scalaVersion := "2.12.6"
gitbucketVersion := "4.23.0"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

scalafmtOnCompile := true