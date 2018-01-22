val Name = "gitbucket-monitorting-plugin"
val Organization = "com.github.YoshinoriN"
val Version = "1.2.0"

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)

name := Name
organization := Organization
version := Version
scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "io.github.gitbucket" %% "gitbucket"          % "4.10.0" % "provided",
  "javax.servlet"        % "javax.servlet-api"  % "3.1.0"  % "provided"
)

scalacOptions := Seq("-deprecation", "-feature", "-language:postfixOps")
javacOptions in compile ++= Seq("-target", "8", "-source", "8")
useJCenter := true