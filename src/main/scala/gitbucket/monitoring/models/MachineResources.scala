package gitbucket.monitoring.models.machineResources

import java.nio.file._
import scala.sys.process._
import gitbucket.monitoring.utils._
import gitbucket.monitoring.models.OperatingSystem

object Resources {
  def fileStore = Files.getFileStore(Paths.get("."))
}

trait Resources {
  val fileStore = Resources.fileStore
  def operatingSystem = OperatingSystem
  def core = Runtime.getRuntime().availableProcessors()
  def totalSpace = UnitConverter.byteToGB(fileStore.getTotalSpace())
  def freeSpace = UnitConverter.byteToGB(fileStore.getUnallocatedSpace())
  def usedSpace = totalSpace - freeSpace
  def cpu: Either[String, Cpu] = {
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
          Rounding.ceil(100 - resouces.filter(c => c.contains("id")).headOption.getOrElse("-").replace("id","").toDouble).toString
        } catch {
          case e: Exception => "ERROR"
        }
      ))
    } catch {
      //TODO: create logfile.
      case e: Exception => Left("ERROR")
    }
  }
  def memory: Either[String, Memory] = {
    try {
      //Estimated available memory
      //https://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git/commit/?id=34e431b0ae398fc54ea69ff85ec700722c9da773
      val mem = StringUtil.DropAndToArray(Process("free -mt") #| Process("grep Mem") !! ,":" , "\\s+")
      if ((Process("free") #| Process("grep available") #| Process("wc -l") !!).trim != "0") {
        Right(Memory(
          mem(0),
          mem(1),
          mem(2),
          mem(3),
          mem(4),
          mem(5)
        ))
      } else {
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
  def swap: Either[String, Swap] = {
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

trait Action {
  self: Resources =>
    def cpu: Either[String, Cpu] = {
      cpu
    }
    def memory: Either[String, Memory] = {
      memory
    }
    def swap: Either[String, Swap] = {
      swap
    }
}

trait Linux extends Resources {

}

trait Mac extends Resources {

}

trait Windows extends Resources {
  override def cpu: Either[String, Cpu] = {
    try {
      Right(Cpu(
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        (Process("powershell -Command Get-WmiObject Win32_PerfFormattedData_PerfOS_Processor | Where-Object {$_.Name -eq '_Total'} | %{ $_.PercentProcessorTime }") !!).toString
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }
  override def memory: Either[String, Memory] = {
    try {
      val totalMem = (Process("powershell -Command Get-WmiObject -Class Win32_PhysicalMemory | %{ $_.Capacity} | Measure-Object -Sum | %{ ($_.sum /1024/1024) }") !!).toDouble
      val availableMem = (Process("powershell -Command Get-WmiObject -Class Win32_PerfFormattedData_PerfOS_Memory | %{ $_.AvailableMBytes}") !!).toDouble
      Right(Memory(
        totalMem.toString,
        (totalMem - availableMem).toString,
        "-",
        "-",
        //(Process("powershell -Command Get-WmiObject -Class Win32_PerfFormattedData_PerfOS_Memory | %{ $_.CacheBytes /1024/1024 }") !!),
        "-",
        availableMem.toString
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }
  override def swap: Either[String, Swap] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}

trait Other extends Resources {
  override def cpu: Either[String, Cpu] = {
    Left(OperatingSystem.notSupportedMessage)
  }
  override def memory: Either[String, Memory] = {
    Left(OperatingSystem.notSupportedMessage)
  }
  override def swap: Either[String, Swap] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}