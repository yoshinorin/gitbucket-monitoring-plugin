package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import scala.sys.process.Process
import scala.util.Try
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.utils._

class Linux extends SystemInformation with MachineResources with ProcessInfo {

  override def getUpTime: Try[Option[UpTime]] = Try {
    val ut = Process("cat /proc/uptime").!!.split(" ")
    val dt = Time.secondsToDateTime(Rounding.ceil(BigDecimal(ut(0)), 0).toInt)
    Some(
      UpTime(
        dt.days.toString + " days " + dt.hours.toString + " hours " + dt.minutes.toString + " minutes ",
        Process("uptime -s").!!
      )
    )
  }

}
