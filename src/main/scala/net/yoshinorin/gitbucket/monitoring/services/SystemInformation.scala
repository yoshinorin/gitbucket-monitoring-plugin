package net.yoshinorin.gitbucket.monitoring.services

import java.time._
import java.nio.file.{Files, Paths}
import scala.sys.process.Process
import scala.util.Try

trait SystemInformation {

  val timeZone: ZoneId = ZoneId.systemDefault()
  val onDocker: Boolean = {
    Files.exists(Paths.get("/.dockerenv"))
  }

  def getCurrentTime: LocalDateTime = LocalDateTime.now()
  def getZoneOffset: ZoneOffset = timeZone.getRules().getOffset(getCurrentTime)
  def getDayOfWeek: DayOfWeek = getCurrentTime.getDayOfWeek()

  def getUpTime: Try[Option[UpTime]] = Try {
    val result = Process("uptime").!!
    val list = result.drop(result.indexOf("up") + 2).split(",")
    Some(UpTime(list(0), Process("uptime -s").!!))
  }

}

case class UpTime(
  upTime: String,
  startTime: String
)
