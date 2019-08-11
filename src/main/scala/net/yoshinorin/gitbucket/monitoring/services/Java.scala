package net.yoshinorin.gitbucket.monitoring.services

import scala.jdk.CollectionConverters._
import net.yoshinorin.gitbucket.monitoring.utils.Converter.byteConverter

object Java {

  private val memTotal: Long = Runtime.getRuntime.totalMemory().byteToMB
  private val memMax: Long = Runtime.getRuntime.maxMemory().byteToMB

  def getSystemProperties: Map[String, String] = System.getProperties.asScala.toMap

  def getMemoryInfo: Memory = {
    val memFree: Long = Runtime.getRuntime.freeMemory().byteToMB
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
