package gitbucket.monitoring.models

import java.nio.file.{Files, Paths}
import scala.xml.XML
import gitbucket.core.util.StringUtil
import gitbucket.monitoring.utils._

object LogBack {
  val enableLogging = Java.all.contains("logback.configurationFile")
  val confPath = Java.all.getOrElse("logback.configurationFile", "Can not find logback settings.xml")
  val xmlContent: Either[String, String] = {
    if (enableLogging) {
      try {
        val bytes = Files.readAllBytes(Paths.get(confPath))
        (Right(
          StringUtil.convertFromByteArray(bytes)
        ))
      } catch {
        case e: Exception => Left("ERROR")
      }
    } else {
      Left("Dosen't configure Logback")
    }
  }
  val logfilePath: Either[String, String] = {
    if (enableLogging) {
      try {
        val xml = xmlContent match {
          case Left(message) => message
          case Right(s) => {
            XML.loadString(s) \ "appender" \ "file" toString
          }
        }
        (Right(
          xml.replace("<file>","").replace("</file>","")
        ))
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