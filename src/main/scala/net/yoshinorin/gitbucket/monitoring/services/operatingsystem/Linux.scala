package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import scala.sys.process.Process
import scala.util.Try
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.utils.Converter.toDateTimeConverter
import net.yoshinorin.gitbucket.monitoring.utils.DateTime

class Linux extends SystemInformation with MachineResources with ProcessInfo {

  override def getUpTime: Try[Option[UpTime]] = Try {
    val dateTime: DateTime = "%1.0f".format(BigDecimal(Process("cat /proc/uptime").!!.split(" ")(0))).toInt.secondsToDateTime
    Some(
      UpTime(
        dateTime.days.toString + " days " + dateTime.hours.toString + " hours " + dateTime.minutes.toString + " minutes ",
        Process("uptime -s").!!
      )
    )
  }

}
