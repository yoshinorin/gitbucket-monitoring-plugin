package gitbucket.monitoring.models

import scala.sys.process._

class OperatingSystem {
  def osName = System.getProperty("os.name")
  def osVersion = System.getProperty("os.version")
  def osArch = System.getProperty("os.arch")
  def onlyLinuxMessage = "Supports only Linux"
  def isLinux = {
    if (osName.toLowerCase == "linux") {
      true
    } else {
      false
    }
  }

  def distribution: String = {
    if (!isLinux) {
      return onlyLinuxMessage
    }
    try {
      val result = Process("cat /etc/issue") !!
      val d = result.replace("\\n","").replace("\\l","").replace(" ","")
      d
    } catch {
      //TODO: create logfile.
      case e: Exception => "ERROR"
    }
  }
}