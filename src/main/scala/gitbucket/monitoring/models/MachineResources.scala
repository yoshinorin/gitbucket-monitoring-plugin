package gitbucket.monitoring.models

import java.nio.file._
import scala.sys.process._
import gitbucket.monitoring.utils.{ByteConverter}

class MachineResources extends OperatingSystem() {
  def fileStore = Files.getFileStore(Paths.get("."))
  def core = Runtime.getRuntime().availableProcessors()
  def totalSpace = ByteConverter.ByteToGB(fileStore.getTotalSpace())
  def freeSpace = ByteConverter.ByteToGB(fileStore.getUnallocatedSpace())
  def usedSpace = totalSpace - freeSpace
  def cpu: Either[String, Cpu] = {

    if (!super.isLinux) {
      return Left(super.onlyLinuxMessage)
    }

    try {
      val res = Process("top -b -n 1") #| Process("grep Cpu(s)") !!
      val result = res.replace(" ","")
      val resouces = result.drop(res.indexOf(":") + 1).split(",")

      Right(Cpu(
        (100 - resouces.filter(c => c.contains("id")).head.replace("id","").toDouble).toString() + "%",
        resouces.filter(c => c.contains("us")).head.replace("us","") + "%",
        resouces.filter(c => c.contains("sy")).head.replace("sy","") + "%",
        resouces.filter(c => c.contains("ni")).head.replace("ni","") + "%",
        resouces.filter(c => c.contains("id")).head.replace("id","") + "%",
        resouces.filter(c => c.contains("wa")).head.replace("wa","") + "%",
        resouces.filter(c => c.contains("hi")).head.replace("hi","") + "%",
        resouces.filter(c => c.contains("si")).head.replace("si","") + "%",
        resouces.filter(c => c.contains("st")).head.replace("st","") + "%"
      ))
    } catch {
      //TODO: create logfile.
      case e: Exception => Left("ERROR")
    }
  }

  case class Cpu (
    usaga: String,
    us: String,
    sy: String,
    ni: String,
    id: String,
    wa: String,
    hi: String,
    si: String,
    st: String
  )

  case class Memory (
    total: Int,
    free: Int,
    used: Int,
    buffer_cache: Int
  )
}