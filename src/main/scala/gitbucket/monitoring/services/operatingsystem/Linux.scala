package gitbucket.monitoring.services.operatingsystem

import java.util._
import java.time._
import java.nio.file.{Paths, Files}
import scala.sys.process._
import gitbucket.monitoring.services._
import gitbucket.monitoring.utils._

class Linux extends SystemInformation with MachineResources with ProcessInfo with GitBucketLog {
  override def getUpTime: Either [String, UpTime] = {
    try {
      val ut = (Process("cat /proc/uptime") !!).split(" ")
      val dt = Time.secondsToDateTime(Rounding.ceil(BigDecimal(ut(0)),0).toInt)
      Right(UpTime(
        dt match {
          case Left(message) => (message)
          case Right(l) => (l.days.toString + " days " + l.hours.toString + " hours " + l.minutes.toString + " minutes ")
        },
        Process("uptime -s") !!
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }
}