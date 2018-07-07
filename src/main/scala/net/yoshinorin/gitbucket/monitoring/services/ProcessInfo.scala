package net.yoshinorin.gitbucket.monitoring.services

import scala.util.Try
import scala.sys.process.Process
import net.yoshinorin.gitbucket.monitoring.models.{LoadAverage, Tasks}
import net.yoshinorin.gitbucket.monitoring.utils._

trait ProcessInfo {

  def getTasks: Try[Option[Tasks]] = Try {
    val resouces = StringUtil.dropAndToArray((Process("top -b -n 1") #| Process("grep Tasks")).!!, ":", ",")
    Some(
      Tasks(
        resouces.filter(c => c.contains("total")).headOption.getOrElse("-").replace("total", ""),
        resouces.filter(c => c.contains("running")).headOption.getOrElse("-").replace("running", ""),
        resouces.filter(c => c.contains("sleeping")).headOption.getOrElse("-").replace("sleeping", ""),
        resouces.filter(c => c.contains("stopped")).headOption.getOrElse("-").replace("stopped", ""),
        resouces.filter(c => c.contains("zombie")).headOption.getOrElse("-").replace("zombie", "")
      )
    )
  }

  def getLoadAverage: Try[Option[LoadAverage]] = Try {
    val result = Process("uptime").!!
    val list = result.drop(result.indexOf("average:") + 8).split(",")
    Some(LoadAverage(list(0), list(1), list(2)))
  }

}
