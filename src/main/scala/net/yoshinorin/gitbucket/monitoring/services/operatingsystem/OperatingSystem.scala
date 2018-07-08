package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import scala.sys.process.Process
import scala.util.{Failure, Success, Try}
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

  val osVersion: String = osType match {
    case Windows =>
      Try {
        Process("powershell -Command Get-WmiObject Win32_OperatingSystem | %{ $_.Version }").!!
      } match {
        case Success(s) => s
        case Failure(f) => Error.FAILURE.message
      }
    case _ => System.getProperty("os.version")
  }

  val distribution: String = osType match {
    case Linux =>
      Try {
        Process("cat /etc/issue").!!.replace("\\n", "").replace("\\l", "").replace(" ", "")
      } match {
        case Success(s) => s
        case Failure(f) => Error.FAILURE.message
      }
    case _ => "-"
  }

  val getInstance = osType match {
    case OperatingSystem.Linux => new Linux
    case OperatingSystem.Mac => new Mac
    case OperatingSystem.Windows => new Windows
    case _ => new Other
  }

}
