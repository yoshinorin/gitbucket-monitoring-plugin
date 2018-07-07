package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import scala.sys.process.Process
import scala.util.Try
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.utils.Converter.{dropAndToArray, toDateTimeConverter}
import net.yoshinorin.gitbucket.monitoring.utils._

class Linux extends SystemInformation with MachineResources with ProcessInfo {

  override def getUpTime: Try[Option[UpTime]] = Try {
    val dt = Rounding.ceil(BigDecimal(Process("cat /proc/uptime").!!.split(" ")(0)), 0).toInt.secondsToDateTime
    Some(
      UpTime(
        dt.days.toString + " days " + dt.hours.toString + " hours " + dt.minutes.toString + " minutes ",
        Process("uptime -s").!!
      )
    )
  }

}
