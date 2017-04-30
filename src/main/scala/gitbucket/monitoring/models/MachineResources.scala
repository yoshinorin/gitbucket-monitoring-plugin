package gitbucket.monitoring.models

import java.nio.file._
import scala.sys.process._
import gitbucket.monitoring.utils.{ByteConverter}

class MachineResources {
  def fileStore = Files.getFileStore(Paths.get("."))
  def core = Runtime.getRuntime().availableProcessors()
  def totalSpace = ByteConverter.ByteToGB(fileStore.getTotalSpace())
  def freeSpace = ByteConverter.ByteToGB(fileStore.getUnallocatedSpace())
  def usedSpace = totalSpace - freeSpace

  private def topCommandToArray(commandResult: String, splitPattern:String): Array[String] = {
    commandResult.drop(commandResult.indexOf(":") + 1).trim().split(splitPattern)
  }

  def cpu: Either[String, Cpu] = OperatingSystem.osType match {
    case OperatingSystem.Linux => {
      try {
        val resouces = topCommandToArray(Process("top -b -n 1") #| Process("grep Cpu(s)") !! ,",")
        Right(Cpu(
          resouces.filter(c => c.contains("us")).headOption.getOrElse("-").replace("us",""),
          resouces.filter(c => c.contains("sy")).headOption.getOrElse("-").replace("sy",""),
          resouces.filter(c => c.contains("ni")).headOption.getOrElse("-").replace("ni",""),
          resouces.filter(c => c.contains("id")).headOption.getOrElse("-").replace("id",""),
          resouces.filter(c => c.contains("wa")).headOption.getOrElse("-").replace("wa",""),
          resouces.filter(c => c.contains("hi")).headOption.getOrElse("-").replace("hi",""),
          resouces.filter(c => c.contains("si")).headOption.getOrElse("-").replace("si",""),
          resouces.filter(c => c.contains("st")).headOption.getOrElse("-").replace("st",""),
          try {
            (100 - resouces.filter(c => c.contains("id")).headOption.getOrElse("-").replace("id","").toDouble).toString()
          } catch {
            case e: Exception => "ERROR"
          }
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
      Left(OperatingSystem.notSupportedMessage)
    }
  }

  def memory: Either[String, Memory] = OperatingSystem.osType match {
    case OperatingSystem.Linux => {
      try {
        val resouces = topCommandToArray(Process("free -mt") #| Process("grep Mem") !! ,"\\s+")
        Right(Memory(
          resouces(0),
          resouces(1),
          resouces(2)
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
      Left(OperatingSystem.notSupportedMessage)
    }
  }

  case class Cpu (
    us: String,
    sy: String,
    ni: String,
    id: String,
    wa: String,
    hi: String,
    si: String,
    st: String,
    usaga: String
  )

  case class Memory (
    total: String,
    used: String,
    free: String
  )
}