package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import java.io.IOException
import scala.sys.process.Process
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.utils._

class Linux extends SystemInformation with MachineResources with ProcessInfo {

  override def getUpTime: Either[String, UpTime] = {

    try {
      val ut = Process("cat /proc/uptime").!!.split(" ")
      val dt = Time.secondsToDateTime(Rounding.ceil(BigDecimal(ut(0)), 0).toInt)
      Right(
        UpTime(
          dt.days.toString + " days " + dt.hours.toString + " hours " + dt.minutes.toString + " minutes ",
          Process("uptime -s").!!
        ))
    } catch {
      case e: IOException => Left(Message.error)
    }
  }

}
