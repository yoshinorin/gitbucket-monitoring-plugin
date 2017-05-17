package gitbucket.monitoring.models

import java.nio.file._
import scala.sys.process._
import gitbucket.monitoring.utils._

class Process extends ProcessBase with LinuxProcess with MacProcess with WindowsProcess with OtherProcess {
  val instance = OperatingSystem.osType match {
    case OperatingSystem.Linux => new ProcessBase with LinuxProcess
    case OperatingSystem.Mac => new ProcessBase with MacProcess
    case OperatingSystem.Windows => new ProcessBase with WindowsProcess
    case _ => new ProcessBase with OtherProcess
  }
}

trait ProcessBase {
  def getTasks: Either[String, Tasks] = {
    try {
      val resouces = StringUtil.dropAndToArray(Process("top -b -n 1") #| Process("grep Tasks") !!,":" , ",")
      Right(Tasks(
        resouces.filter(c => c.contains("total")).headOption.getOrElse("-").replace("total",""),
        resouces.filter(c => c.contains("running")).headOption.getOrElse("-").replace("running",""),
        resouces.filter(c => c.contains("sleeping")).headOption.getOrElse("-").replace("sleeping",""),
        resouces.filter(c => c.contains("stopped")).headOption.getOrElse("-").replace("stopped",""),
        resouces.filter(c => c.contains("zombie")).headOption.getOrElse("-").replace("zombie","")
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }
  def getLoadAverage: Either[String, LoadAverage] = {
    try {
      val result = Process("uptime") !!
      val list = result.drop(result.indexOf("average:") + 8).split(",")
      Right(LoadAverage(
        list(0),
        list(1),
        list(2)
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }
}

trait LinuxProcess extends ProcessBase {

}

trait MacProcess extends ProcessBase {
  override def getTasks: Either[String, Tasks] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}

trait WindowsProcess extends ProcessBase {
  override def getTasks: Either[String, Tasks] = {
    Left(OperatingSystem.onlyLinuxMessage)
  }
  override def getLoadAverage: Either[String, LoadAverage] = {
    try {
      Right(LoadAverage(
        (Process("powershell -Command Get-WmiObject win32_processor | Measure-Object -property LoadPercentage -Average | Select Average | %{ $_.Average }") !!).toString,
        OperatingSystem.notSupportedMessage,
        OperatingSystem.notSupportedMessage
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }
}

trait OtherProcess extends ProcessBase {
  override def getTasks: Either[String, Tasks] = {
    Left(OperatingSystem.notSupportedMessage)
  }
  override def getLoadAverage: Either[String, LoadAverage] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}

case class Tasks (
  total: String,
  running: String,
  sleeping: String,
  stopped: String,
  zombie: String
)

case class LoadAverage (
  oneMin: String,
  fiveMin: String,
  fifteenMin: String
)