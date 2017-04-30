package gitbucket.monitoring.models

import java.nio.file._
import scala.sys.process._

class Process {
  private def topCommandToArray(commandResult: String, splitPattern:String): Array[String] = {
    commandResult.drop(commandResult.indexOf(":") + 1).trim().split(splitPattern)
  }

  def tasks: Either[String, Tasks] = OperatingSystem.osType match {
    case OperatingSystem.Linux => {
      try {
        val resouces = topCommandToArray(Process("top -b -n 1") #| Process("grep Tasks") !!, ",")
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
    case OperatingSystem.Windows => {
      //TODO: create command for Windows
      Left(OperatingSystem.onlyLinuxMessage)
    }
    case OperatingSystem.Unknown => {
      Left(OperatingSystem.onlyLinuxMessage)
    }
  }

  case class Tasks (
    total: String,
    running: String,
    sleeping: String,
    stopped: String,
    zombie: String
  )
}