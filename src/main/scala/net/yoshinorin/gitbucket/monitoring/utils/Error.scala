package net.yoshinorin.gitbucket.monitoring.utils

sealed abstract class Error(val message: String)

object Error {

  object NOTSUPPORTED extends Error("NOT SUPPORTED")
  object FAILURE extends Error("ERROR")
  object NOTFOUND extends Error("NOT FOUND")

}
