package gitbucket.monitoring.models

import java.nio.file._
import scala.sys.process._
import gitbucket.monitoring.utils._

trait ProcessInfo {
  def getTasks: Either[String, Tasks] = {
    try {
      val resouces = StringUtil.dropAndToArray(Process("top -b -n 1") #| Process("grep Tasks") !!,":" , ",")
      Right(Tasks(
        resouces.filter(c => c.contains("total")).headOption.getOrElse("-").replace("total",""),
        resouces.filter(c => c.contains("running")).headOption.getOrElse("-").replace("running",""),
        resouces.filter(c => c.contains("sleeping")).headOption.getOrElse("-").replace("sleeping",""),
        resouces.filter(c => c.contains("stopped")).headOption.getOrElse("-").replace("stopped",""),
        resouces.filter(c => c.contains("zombie")).headOption.getOrElse("-").replace("zombie","")
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }

  def getLoadAverage: Either[String, LoadAverage] = {
    try {
      val result = Process("uptime") !!
      val list = result.drop(result.indexOf("average:") + 8).split(",")
      Right(LoadAverage(
        list(0),
        list(1),
        list(2)
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }
}

case class Tasks (
  total: String,
  running: String,
  sleeping: String,
  stopped: String,
  zombie: String
)

case class LoadAverage (
  oneMin: String,
  fiveMin: String,
  fifteenMin: String
)