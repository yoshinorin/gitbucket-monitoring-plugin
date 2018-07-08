package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import scala.sys.process.Process
import scala.util.Try
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.utils.Converter.toDateTimeConverter

class Linux extends SystemInformation with MachineResources with ProcessInfo {

  override def getUpTime: Try[Option[UpTime]] = Try {
    val dt = "%1.0f".format(BigDecimal(Process("cat /proc/uptime").!!.split(" ")(0))).toInt.secondsToDateTime
    Some(
      UpTime(
        dt.days.toString + " days " + dt.hours.toString + " hours " + dt.minutes.toString + " minutes ",
        Process("uptime -s").!!
      )
    )
  }

}
