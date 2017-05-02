package gitbucket.monitoring.models

import scala.sys.process._

object OperatingSystem {
  sealed abstract class OSType
  case object Linux extends OSType
  case object Windows extends OSType
  case object Mac extends OSType
  case object Other extends OSType

  def osVersion = System.getProperty("os.version")
  def osArch = System.getProperty("os.arch")
  val osName = System.getProperty("os.name")
  val onlyLinuxMessage = "Supports only Linux"
  val notSupportedMessage = "Not supported"
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