package gitbucket.monitoring.models.systemInformation

import java.util._
import java.time._
import java.nio.file.{Paths, Files}
import scala.sys.process._
import gitbucket.monitoring.models.OperatingSystem

object Info {
  def timeZone = ZoneId.systemDefault()
  def onDocker: Boolean = {
    try {
      Files.exists(Paths.get("/.dockerenv"))
    } catch {
      case e: Exception => false
    }
  }
}

trait Info {
  def operatingSystem = OperatingSystem
  val onDocker = Info.onDocker
  val timeZone = Info.timeZone
  def nowTime = LocalDateTime.now()
  def zoneOffset = timeZone.getRules().getOffset(nowTime)
  def dayOfWeek = nowTime.getDayOfWeek()
  def upTime: Either[String, UpTime] = {
    try {
      val result = Process("uptime") !!
      val list = result.drop(result.indexOf("up") + 2).split(",")
      Right(UpTime(
        list(0),
        Process("uptime -s") !!
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }

  case class UpTime (
    uptime: String,
    startTime: String
  )
}

trait Action {
  self: Info =>
    def upTime: Either[String, UpTime] = {
      upTime
    }
}

trait Linux extends Info {

}

trait Mac extends Info {

}

trait Windows extends Info {
  override def upTime: Either[String, UpTime] = {
    try {
      Right(UpTime(
        (Process("powershell -Command \"&{$os=Get-WmiObject win32_operatingsystem;$time=((Get-Date) - $os.ConvertToDateTime($os.lastbootuptime)); $time.Days.ToString() + \\\" days \\\" +  $time.Hours.ToString() + \\\" hours \\\" + $time.Minutes.ToString() + \\\" minutes \\\"}\"") !!),
        (Process("powershell -Command [Management.ManagementDateTimeConverter]::ToDateTime((Get-WmiObject Win32_OperatingSystem).LastBootUpTime)") !!)
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }
}

trait Other extends Info {
  override def upTime: Either[String, UpTime] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}