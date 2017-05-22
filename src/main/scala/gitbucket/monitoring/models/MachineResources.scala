package gitbucket.monitoring.models

import java.nio.file._
import scala.sys.process._
import gitbucket.monitoring.utils._

trait MachineResourcesBase {
  private val fileStore = Files.getFileStore(Paths.get("."))
  def cpuCore = Runtime.getRuntime().availableProcessors()
  def getCpu: Either[String, Cpu] = {
    try {
      val resouces = StringUtil.dropAndToArray(Process("top -b -n 1") #| Process("grep Cpu(s)") !! ,":" , ",")
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
          case e: Exception => Message.error
        }
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }
  def getMemory: Either[String, Memory] = {
    try {
      //Estimated available memory
      //https://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git/commit/?id=34e431b0ae398fc54ea69ff85ec700722c9da773
      val mem = StringUtil.dropAndToArray(Process("free -mt") #| Process("grep Mem") !! ,":" , "\\s+")
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
      case e: Exception => Left(Message.error)
    }
  }
  def getSwap: Either[String, Swap] = {
    try {
      val swap =  StringUtil.dropAndToArray(Process("free -mt") #| Process("grep Swap") !! ,":" , "\\s+")
      Right(Swap(
        swap(0),
        swap(1),
        swap(2)
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }
  def getDiskSpace: DiskSpace = {
    val totalSpace = UnitConverter.byteToGB(fileStore.getTotalSpace())
    val freeSpace = UnitConverter.byteToGB(fileStore.getUnallocatedSpace())
    val usedSpace = totalSpace - freeSpace
    DiskSpace(
      totalSpace.toString,
      freeSpace.toString,
      usedSpace.toString
    )
  }
}

class MachineResources extends MachineResourcesBase {
  val instance = OperatingSystem.osType match {
    case OperatingSystem.Linux => new MachineResourcesBase with Linux
    case OperatingSystem.Mac => new MachineResourcesBase with Mac
    case OperatingSystem.Windows => new MachineResourcesBase with Windows
    case _ => new MachineResourcesBase with Other
  }

  trait Linux extends MachineResourcesBase {

  }

  trait Mac extends MachineResourcesBase {
    override def getCpu: Either[String, Cpu] = {
      Left(Message.notSupported)
    }
  }

  trait Windows extends MachineResourcesBase {
    override def getCpu: Either[String, Cpu] = {
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
        case e: Exception => Left(Message.error)
      }
    }
    override def getMemory: Either[String, Memory] = {
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
        case e: Exception => Left(Message.error)
      }
    }
    override def getSwap: Either[String, Swap] = {
      Left(Message.notSupported)
    }
  }

  trait Other extends MachineResourcesBase {
    override def getCpu: Either[String, Cpu] = {
      Left(Message.notSupported)
    }
    override def getMemory: Either[String, Memory] = {
      Left(Message.notSupported)
    }
    override def getSwap: Either[String, Swap] = {
      Left(Message.notSupported)
    }
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

case class DiskSpace (
  totalSpace: String,
  freeSpace: String,
  usedSpace: String
)