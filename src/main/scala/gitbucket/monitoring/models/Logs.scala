package gitbucket.monitoring.models

import gitbucket.monitoring.utils._

object LogBack {

  val enableLogging = Java.all.contains("logback.configurationFile")
  val confPath = Java.all.getOrElse("logback.configurationFile", "Can not find logback-settings.xml")

  def xmlContent: Either[String, String] = {
    if (enableLogging) {
      try {
        val s = scala.io.Source.fromFile(confPath)
        try {
          (Right(
            s.mkString
          ))
        } catch {
          case e: Exception => Left("ERROR")
        } finally {
          s.close
        }
      } catch {
        case e: Exception => Left("ERROR")
      }
    } else {
      Left("Dosen't configure Logback")
    }
  }
}

class Logs () {
  val logBack = LogBack
}