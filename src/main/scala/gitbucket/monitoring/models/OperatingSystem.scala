package gitbucket.monitoring.models

import scala.sys.process._

object OperatingSystem {
  sealed abstract class OSType
  case object Linux extends OSType
  case object Windows extends OSType
  case object Unknown extends OSType

  def osVersion = System.getProperty("os.version")
  def osArch = System.getProperty("os.arch")
  def onlyLinuxMessage = "Supports only Linux"
  def osType: OSType = {
    if (System.getProperty("os.name").toLowerCase == "linux") {
      Linux
    } else if (System.getProperty("os.name").toLowerCase == "windows")  {
      Windows
    } else {
      Unknown
    }
  }

  def distribution: String = osType match {
    case Linux => {
      try {
        val result = Process("cat /etc/issue") !!
        val d = result.replace("\\n","").replace("\\l","").replace(" ","")
        d
      } catch {
        //TODO: create logfile.
        case e: Exception => "ERROR"
      }
    }
    case _ => {
      onlyLinuxMessage
    }
  }
}