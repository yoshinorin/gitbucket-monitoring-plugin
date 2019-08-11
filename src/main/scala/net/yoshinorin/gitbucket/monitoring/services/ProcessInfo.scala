package net.yoshinorin.gitbucket.monitoring.services

import scala.util.Try
import scala.sys.process.Process
import net.yoshinorin.gitbucket.monitoring.models.{LoadAverage, Tasks}
import net.yoshinorin.gitbucket.monitoring.utils.Converter.dropAndToArray

trait ProcessInfo {

  def getTasks: Try[Option[Tasks]] = Try {
    val resouces: Array[String] = (Process("top -b -n 1") #| Process("grep Tasks")).!!.dropAndToArray(":", ",")
    Some(
      Tasks(
        resouces.find(c => c.contains("total")).getOrElse("-").replace("total", ""),
        resouces.find(c => c.contains("running")).getOrElse("-").replace("running", ""),
        resouces.find(c => c.contains("sleeping")).getOrElse("-").replace("sleeping", ""),
        resouces.find(c => c.contains("stopped")).getOrElse("-").replace("stopped", ""),
        resouces.find(c => c.contains("zombie")).getOrElse("-").replace("zombie", "")
      )
    )
  }

  def getLoadAverage: Try[Option[LoadAverage]] = Try {
    val result = Process("uptime").!!
    val list = result.drop(result.indexOf("average:") + 8).split(",")
    Some(LoadAverage(list(0), list(1), list(2)))
  }

}
