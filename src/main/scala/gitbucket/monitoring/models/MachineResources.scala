package gitbucket.monitoring.models

import java.nio.file._
import scala.sys.process._
import gitbucket.monitoring.utils._

class MachineResources {
  def fileStore = Files.getFileStore(Paths.get("."))
  def core = Runtime.getRuntime().availableProcessors()
  def totalSpace = ByteConverter.ByteToGB(fileStore.getTotalSpace())
  def freeSpace = ByteConverter.ByteToGB(fileStore.getUnallocatedSpace())
  def usedSpace = totalSpace - freeSpace

  def cpu: Either[String, Cpu] = OperatingSystem.osType match {
    case OperatingSystem.Linux => {
      try {
        val resouces = StringUtil.DropAndToArray(Process("top -b -n 1") #| Process("grep Cpu(s)") !! ,":" , ",")
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
    case _ => {
      Left(OperatingSystem.notSupportedMessage)
    }
  }

  def memory: Either[String, Memory] = OperatingSystem.osType match {
    case OperatingSystem.Linux | OperatingSystem.Mac => {
      try {
        var n = (Process("free") #| Process("grep available") #| Process("wc -l") !!)
        //Estimated available memory
        //https://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git/commit/?id=34e431b0ae398fc54ea69ff85ec700722c9da773
        if (n.toString().trim() != "0") {
          val mem = StringUtil.DropAndToArray(Process("free -mt") #| Process("grep Mem") !! ,":" , "\\s+")
          Right(Memory(
            mem(0),
            mem(1),
            mem(2),
            mem(3),
            mem(4),
            mem(5)
          ))
        } else {
          val mem = StringUtil.DropAndToArray(Process("free -mt") #| Process("grep Mem") !! ,":", "\\s+")
          Right(Memory(
            mem(0),
            mem(1),
            mem(2),
            mem(3),
            (mem(4).toInt + mem(5).toInt).toString,
            (mem(2).toInt + mem(4).toInt + mem(5).toInt).toString
          ))
        }
      } catch {
        //TODO: create logfile.
        case e: Exception => Left("ERROR")
      }
    }
    case OperatingSystem.Windows => {
      //TODO: create command for Windows
      Left(OperatingSystem.onlyLinuxMessage)
    }
    case _ => {
      Left(OperatingSystem.notSupportedMessage)
    }
  }

  def swap: Either[String, Swap] = OperatingSystem.osType match {
    case OperatingSystem.Linux | OperatingSystem.Mac => {
      try {
        val swap =  StringUtil.DropAndToArray(Process("free -mt") #| Process("grep Swap") !! ,":" , "\\s+")
        Right(Swap(
          swap(0),
          swap(1),
          swap(2)
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
    case _ => {
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
    free: String,
    shared: String,
    buff_cache: String,
    available: String
  )

  case class Swap (
   total: String,
   used: String,
   free: String
  )
}