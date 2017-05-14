package gitbucket.monitoring.models

import scala.collection.JavaConversions._
import gitbucket.monitoring.utils._

object Java {

  def getSystemProperties: Map[String, String] = System.getProperties().toMap

  val memTotal = (UnitConverter.byteToMB(Runtime.getRuntime().totalMemory()))
  val memMax = (UnitConverter.byteToMB(Runtime.getRuntime().maxMemory()))

  def getMemoryInfo: Memory = {
    val memFree = UnitConverter.byteToMB(Runtime.getRuntime().freeMemory())
    Memory(
      memTotal.toString,
      memMax.toString,
      memFree.toString,
      (memTotal - memFree).toString
    )
  }

  case class Memory(
    total: String,
    max: String,
    free: String,
    used: String
  )
}