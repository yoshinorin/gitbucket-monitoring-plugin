package gitbucket.monitoring.services

import java.nio.file.{Files, Paths}
import scala.xml.XML
import gitbucket.core.util.StringUtil
import gitbucket.monitoring.models.{LogBackInfo}
import gitbucket.monitoring.utils._

object LogBack {
  val notFoundMessage = "Can not find logback configuration file."
  val dosentConfigureMessage = "Dosen't configure Logback."
  val enableLogging = Java.getSystemProperties.contains("logback.configurationFile")
  val confPath = Java.getSystemProperties.getOrElse("logback.configurationFile", notFoundMessage)

  def getLogBackSettings: Either[String, String] = {
    if (enableLogging) {
      try {
        val bytes = Files.readAllBytes(Paths.get(confPath))
        (Right(
          StringUtil.convertFromByteArray(bytes)
        ))
      } catch {
        case e: Exception => Left(Message.error)
      }
    } else {
      Left(dosentConfigureMessage)
    }
  }

  def getLogBackInfo: LogBackInfo = {
    val logFilePath: Either[String, String] = {
      if (enableLogging) {
        try {
          val xml = getLogBackSettings match {
            case Left(message) => message
            case Right(s) => {
              (XML.loadString(s) \\ "appender" \ "file" toString).replace("<file>","").replace("</file>","")
            }
          }
          if (xml.trim.length == 0) {
            Left(Message.notFound)
          } else {
            (Right(
              xml
            ))
          }
        } catch {
          case e: Exception => Left(Message.error)
        }
      } else {
        Left(dosentConfigureMessage)
      }
    }

    LogBackInfo(
      Java.getSystemProperties.contains("logback.configurationFile"),
      Java.getSystemProperties.getOrElse("logback.configurationFile", notFoundMessage),
      logFilePath
    )
  }
}