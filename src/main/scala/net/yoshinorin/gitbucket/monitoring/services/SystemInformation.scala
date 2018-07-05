package net.yoshinorin.gitbucket.monitoring.services

import java.io.IOException
import java.time._
import java.nio.file.{Files, Paths}
import scala.sys.process.Process
import net.yoshinorin.gitbucket.monitoring.utils._

trait SystemInformation {

  val timeZone = ZoneId.systemDefault()
  val onDocker: Boolean = {
    Files.exists(Paths.get("/.dockerenv"))
  }

  def getCurrentTime = LocalDateTime.now()
  def getZoneOffset = timeZone.getRules().getOffset(getCurrentTime)
  def getDayOfWeek = getCurrentTime.getDayOfWeek()

  def getUpTime: Either[String, UpTime] = {
    try {
      val result = Process("uptime").!!
      val list = result.drop(result.indexOf("up") + 2).split(",")
      Right(
        UpTime(
          list(0),
          Process("uptime -s").!!
        ))
    } catch {
      case e: IOException => Left(Message.error)
    }
  }

}

case class UpTime(
  upTime: String,
  startTime: String
)
