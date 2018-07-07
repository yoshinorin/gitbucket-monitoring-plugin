package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import java.io.IOException
import scala.sys.process.Process
import scala.util.Try
import net.yoshinorin.gitbucket.monitoring.utils.Error

object OperatingSystem {

  sealed abstract class OSType
  case object Linux extends OSType
  case object Windows extends OSType
  case object Mac extends OSType
  case object Other extends OSType

  val osArch = System.getProperty("os.arch")
  val osName = System.getProperty("os.name")

  val osType: OSType =
    if (osName.toLowerCase.contains("linux")) Linux
    else if (osName.toLowerCase.contains("windows")) Windows
    else if (osName.toLowerCase.contains("mac")) Mac
    else Other

  val osVersion: Try[String] = osType match {
    case Windows =>
      Try {
        Process("powershell -Command Get-WmiObject Win32_OperatingSystem | %{ $_.Version }").!!
      }.recover {
        case e: IOException => Error.FAILURE.message
      }
    case _ =>
      Try {
        System.getProperty("os.version")
      }
  }

  val distribution: Try[String] = osType match {
    case Linux =>
      Try {
        Process("cat /etc/issue").!!.replace("\\n", "").replace("\\l", "").replace(" ", "")
      }.recover {
        case e: IOException => Error.FAILURE.message
      }
    case _ =>
      Try {
        "-"
      }
  }

  val getInstance = osType match {
    case OperatingSystem.Linux => new Linux
    case OperatingSystem.Mac => new Mac
    case OperatingSystem.Windows => new Windows
    case _ => new Other
  }

}
