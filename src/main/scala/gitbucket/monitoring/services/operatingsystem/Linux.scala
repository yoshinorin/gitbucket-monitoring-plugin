package gitbucket.monitoring.services.operatingsystem

import scala.sys.process._
import gitbucket.monitoring.services._
import gitbucket.monitoring.utils._

class Linux extends SystemInformation with MachineResources with ProcessInfo with GitBucketLog {
  override def getUpTime: Either[String, UpTime] = {
    try {
      val ut = (Process("cat /proc/uptime") !!).split(" ")
      val dt = Time.secondsToDateTime(Rounding.ceil(BigDecimal(ut(0)), 0).toInt)
      Right(
        UpTime(
          dt.days.toString + " days " + dt.hours.toString + " hours " + dt.minutes.toString + " minutes ",
          Process("uptime -s") !!
        ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }
}
