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
  def cpu: Cpu = {
    if (!super.isLinux) {
      return new Cpu()
    }
    try {
      val res = Process("top -b -n 1") #| Process("grep Cpu(s)") !!
      val result = res.replace(" ","")
      val resouces = result.drop(res.indexOf(":") + 1).split(",")

      Cpu(
        (100 - resouces.filter(c => c.contains("id")).head.replace("id","").toDouble).toString() + "%",
        resouces.filter(c => c.contains("us")).head.replace("us","") + "%",
        resouces.filter(c => c.contains("sy")).head.replace("sy","") + "%",
        resouces.filter(c => c.contains("ni")).head.replace("ni","") + "%",
        resouces.filter(c => c.contains("id")).head.replace("id","") + "%",
        resouces.filter(c => c.contains("wa")).head.replace("wa","") + "%",
        resouces.filter(c => c.contains("hi")).head.replace("hi","") + "%",
        resouces.filter(c => c.contains("si")).head.replace("si","") + "%",
        resouces.filter(c => c.contains("st")).head.replace("st","") + "%"
      )
    } catch {
      case e:Exception => {
        //TODO: put logfile.
        return new Cpu (
          "ERROR",
          "ERROR",
          "ERROR",
          "ERROR",
          "ERROR",
          "ERROR",
          "ERROR",
          "ERROR",
          "ERROR"
        )
      }
    }
  }
  case class Cpu (
    usaga: String = "Only support Linux",
    us: String = "Only support Linux",
    sy: String = "Only support Linux",
    ni: String = "Only support Linux",
    id: String = "Only support Linux",
    wa: String = "Only support Linux",
    hi: String = "Only support Linux",
    si: String = "Only support Linux",
    st: String = "Only support Linux"
  )

}