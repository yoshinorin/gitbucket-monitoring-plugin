package gitbucket.monitoring.models

import scala.sys.process._
import gitbucket.monitoring.utils.Message

object OperatingSystem {
  sealed abstract class OSType
  case object Linux extends OSType
  case object Windows extends OSType
  case object Mac extends OSType
  case object Other extends OSType

  val osArch = System.getProperty("os.arch")
  val osName = System.getProperty("os.name")
  
  def osType: OSType = {
    if (osName.toLowerCase.contains("linux")) {
      Linux
    } else if (osName.toLowerCase.contains("windows")) {
      Windows
    } else if (osName.toLowerCase.contains("mac")) {
      Mac
    } else {
      Other
    }
  }

  def osVersion:String = osType match {
    case Windows => {
      (Process("powershell -Command Get-WmiObject Win32_OperatingSystem | %{ $_.Version }") !!).toString
    }
    case _ => {
      System.getProperty("os.version")
    }
  }

  def distribution: String = osType match {
    case Linux => {
      try {
        val result = Process("cat /etc/issue") !!
        val d = result.replace("\\n","").replace("\\l","").replace(" ","")
        d
      } catch {
        case e: Exception => Message.error
      }
    }
    case _ => {
      "-"
    }
  }
}