package gitbucket.monitoring.models

import java.util._
import java.time._
import java.nio.file.{Paths, Files}
import scala.sys.process._

class SystemInformation {
  def operatingSystem = OperatingSystem
  def nowTime = LocalDateTime.now()
  def timeZone = ZoneId.systemDefault()
  def zoneOffset = timeZone.getRules().getOffset(nowTime)
  def dayOfWeek = nowTime.getDayOfWeek()

  def onDocker: Boolean = {
    try {
      Files.exists(Paths.get("/.dockerenv"))
    } catch {
      case e: Exception => false
    }
  }

  def upTime: Either[String, UpTime] = OperatingSystem.osType match {
    case OperatingSystem.Linux => {
      val result = Process("uptime") !!
      val list = result.drop(result.indexOf("up") + 2).split(",")

      try {
        Right(UpTime(
          list(0),
          Process("uptime -s") !!
        ))
      } catch {
        case e: Exception => Left("ERROR")
      }
    }
    case OperatingSystem.Windows => {
      //TODO: create command for Windows
      Left(OperatingSystem.onlyLinuxMessage)
    }
    case OperatingSystem.Unknown => {
      Left(OperatingSystem.notSupportedMessage)
    }
  }

  case class UpTime (
    uptime: String,
    startTime: String
  )
}