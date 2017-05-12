package gitbucket.monitoring.models

import scala.collection.JavaConversions._
import gitbucket.monitoring.utils._

object Java {
  def all: Map[String, String] = System.getProperties().toMap
}

class Java () {
  val all = Java.all
  val memTotal = (UnitConverter.byteToMB(Runtime.getRuntime().totalMemory()))
  val memFree = (UnitConverter.byteToMB(Runtime.getRuntime().freeMemory()))
  val memUsed = memTotal - memFree
  val memMax = (UnitConverter.byteToMB(Runtime.getRuntime().maxMemory()))
}