package net.yoshinorin.gitbucket.monitoring.services

import java.nio.file.{Files, Paths}
import scala.sys.process.Process
import scala.util.{Failure, Success, Try}
import net.yoshinorin.gitbucket.monitoring.models.{Cpu, DiskSpace, Memory, Swap}
import net.yoshinorin.gitbucket.monitoring.utils.Converter.{byteConverter, dropAndToArray}
import net.yoshinorin.gitbucket.monitoring.utils.Error

trait MachineResources {

  val cpuCore: Int = Runtime.getRuntime().availableProcessors()

  def getCpu: Try[Option[Cpu]] = Try {
    val resouces: Array[String] = (Process("top -d 0.3 -b -n 2") #| Process("grep Cpu(s)") #| Process("tail -n 1")).!!.dropAndToArray(":", ",")
    Some(
      Cpu(
        resouces.filter(c => c.contains("us")).headOption.getOrElse("-").replace("us", ""),
        resouces.filter(c => c.contains("sy")).headOption.getOrElse("-").replace("sy", ""),
        resouces.filter(c => c.contains("ni")).headOption.getOrElse("-").replace("ni", ""),
        resouces.filter(c => c.contains("id")).headOption.getOrElse("-").replace("id", ""),
        resouces.filter(c => c.contains("wa")).headOption.getOrElse("-").replace("wa", ""),
        resouces.filter(c => c.contains("hi")).headOption.getOrElse("-").replace("hi", ""),
        resouces.filter(c => c.contains("si")).headOption.getOrElse("-").replace("si", ""),
        resouces.filter(c => c.contains("st")).headOption.getOrElse("-").replace("st", ""),
        Try {
          "%1.2f".format((100 - resouces.filter(c => c.contains("id")).headOption.getOrElse("-").replace("id", "").toDouble))
        } match {
          case Success(s) => s
          case Failure(f) => Error.FAILURE.message
        }
      )
    )
  }

  def getMemory: Try[Option[Memory]] = Try {
    //Estimated available memory
    //https://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git/commit/?id=34e431b0ae398fc54ea69ff85ec700722c9da773
    val mem: Array[String] = (Process("free -mt") #| Process("grep Mem")).!!.dropAndToArray(":", "\\s+")
    if ((Process("free") #| Process("grep available") #| Process("wc -l")).!!.trim != "0") {
      Some(Memory(mem(0), mem(1), mem(2), mem(3), mem(4), mem(5)))
    } else {
      Some(
        Memory(
          mem(0),
          mem(1),
          mem(2),
          mem(3),
          Try {
            (mem(4).toInt + mem(5).toInt).toString
          } match {
            case Success(s) => s
            case Failure(f) => Error.FAILURE.message
          },
          Try {
            (mem(2).toInt + mem(4).toInt + mem(5).toInt).toString
          } match {
            case Success(s) => s
            case Failure(f) => Error.FAILURE.message
          }
        )
      )
    }
  }

  def getSwap: Try[Option[Swap]] = Try {
    val swap: Array[String] = (Process("free -mt") #| Process("grep Swap")).!!.dropAndToArray(":", "\\s+")
    Some(Swap(swap(0), swap(1), swap(2)))
  }

  def getDiskSpace: DiskSpace = {
    val totalSpace: Long = Files.getFileStore(Paths.get(".")).getTotalSpace().byteToGB
    val freeSpace: Long = Files.getFileStore(Paths.get(".")).getUnallocatedSpace().byteToGB
    val usedSpace: Long = totalSpace - freeSpace

    DiskSpace(
      totalSpace.toString,
      freeSpace.toString,
      usedSpace.toString
    )
  }

}
