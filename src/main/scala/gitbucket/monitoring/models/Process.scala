package gitbucket.monitoring.models.processInformation

import java.nio.file._
import scala.sys.process._
import gitbucket.monitoring.utils._
import gitbucket.monitoring.models.OperatingSystem

trait Info {
  def tasks: Either[String, Tasks] = {
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
      //TODO: create logfile.
      case e: Exception => Left("ERROR")
    }
  }
  def loadAve: Either[String, LoadAve] = {
    try {
      val result = Process("uptime") !!
      val list = result.drop(result.indexOf("average:") + 8).split(",")
      Right(LoadAve(
        list(0),
        list(1),
        list(2)
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }

  case class Tasks (
    total: String,
    running: String,
    sleeping: String,
    stopped: String,
    zombie: String
  )

  case class LoadAve (
    oneMin: String,
    fiveMin: String,
    fifteenMin: String
  )
}

trait Action {
  self: Info =>
    def tasks: Either[String, Tasks] = {
      tasks
    }
    def loadAve: Either[String, LoadAve] = {
      loadAve
    }
}

trait Linux extends Info {

}

trait Mac extends Info {
  override def tasks: Either[String, Tasks] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}

trait Windows extends Info {
  override def tasks: Either[String, Tasks] = {
    Left(OperatingSystem.onlyLinuxMessage)
  }
  override def  loadAve: Either[String, LoadAve] = {
    try {
      Right(LoadAve(
        (Process("powershell -Command Get-WmiObject win32_processor | Measure-Object -property LoadPercentage -Average | Select Average | %{ $_.Average }") !!).toString,
        OperatingSystem.notSupportedMessage,
        OperatingSystem.notSupportedMessage
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }
}

trait Other extends Info {
  override def tasks: Either[String, Tasks] = {
    Left(OperatingSystem.notSupportedMessage)
  }
  override def  loadAve: Either[String, LoadAve] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}