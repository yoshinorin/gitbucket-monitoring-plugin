package net.yoshinorin.gitbucket.monitoring.services

import java.nio.file._
import scala.sys.process._
import net.yoshinorin.gitbucket.monitoring.models.{Cpu, Memory, Swap, DiskSpace}
import net.yoshinorin.gitbucket.monitoring.utils._

trait MachineResources {
  private val fileStore = Files.getFileStore(Paths.get("."))

  val cpuCore = Runtime.getRuntime().availableProcessors()

  def getCpu: Either[String, Cpu] = {
    try {
      val resouces = StringUtil.dropAndToArray(Process("top -b -n 1") #| Process("grep Cpu(s)") !!, ":", ",")
      Right(
        Cpu(
          resouces.filter(c => c.contains("us")).headOption.getOrElse("-").replace("us", ""),
          resouces.filter(c => c.contains("sy")).headOption.getOrElse("-").replace("sy", ""),
          resouces.filter(c => c.contains("ni")).headOption.getOrElse("-").replace("ni", ""),
          resouces.filter(c => c.contains("id")).headOption.getOrElse("-").replace("id", ""),
          resouces.filter(c => c.contains("wa")).headOption.getOrElse("-").replace("wa", ""),
          resouces.filter(c => c.contains("hi")).headOption.getOrElse("-").replace("hi", ""),
          resouces.filter(c => c.contains("si")).headOption.getOrElse("-").replace("si", ""),
          resouces.filter(c => c.contains("st")).headOption.getOrElse("-").replace("st", ""),
          try {
            Rounding.ceil(100 - resouces.filter(c => c.contains("id")).headOption.getOrElse("-").replace("id", "").toDouble).toString
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
      val mem = StringUtil.dropAndToArray(Process("free -mt") #| Process("grep Mem") !!, ":", "\\s+")
      if ((Process("free") #| Process("grep available") #| Process("wc -l") !!).trim != "0") {
        Right(
          Memory(
            mem(0),
            mem(1),
            mem(2),
            mem(3),
            mem(4),
            mem(5)
          ))
      } else {
        Right(
          Memory(
            mem(0),
            mem(1),
            mem(2),
            mem(3),
            try {
              (mem(4).toInt + mem(5).toInt).toString
            } catch {
              case e: Exception => Message.error
            },
            try {
              (mem(2).toInt + mem(4).toInt + mem(5).toInt).toString
            } catch {
              case e: Exception => Message.error
            }
          ))
      }
    } catch {
      case e: Exception => Left(Message.error)
    }
  }

  def getSwap: Either[String, Swap] = {
    try {
      val swap = StringUtil.dropAndToArray(Process("free -mt") #| Process("grep Swap") !!, ":", "\\s+")
      Right(
        Swap(
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
