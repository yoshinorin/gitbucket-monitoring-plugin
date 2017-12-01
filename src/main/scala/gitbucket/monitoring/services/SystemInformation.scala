package gitbucket.monitoring.services

import java.time._
import java.nio.file.{Paths, Files}
import scala.sys.process._
import gitbucket.monitoring.utils._

trait SystemInformation {
  val timeZone = ZoneId.systemDefault()
  def nowTime = LocalDateTime.now()
  def zoneOffset = timeZone.getRules().getOffset(nowTime)
  def dayOfWeek = nowTime.getDayOfWeek()

  val onDocker: Boolean = {
    Files.exists(Paths.get("/.dockerenv"))
  }

  def getUpTime: Either[String, UpTime] = {
    try {
      val result = Process("uptime") !!
      val list = result.drop(result.indexOf("up") + 2).split(",")
      Right(UpTime(
        list(0),
        Process("uptime -s") !!
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }
}

case class UpTime (
  uptime: String,
  startTime: String
)