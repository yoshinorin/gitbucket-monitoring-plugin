package gitbucket.monitoring.models

import gitbucket.monitoring.utils.{ByteConverter}

object JVM {
  def vmName = System.getProperty("java.vm.name")
  def javaVersion = System.getProperty("java.version")
}

class JVM () {
  val vmName = JVM.vmName
  val javaVersion = JVM.javaVersion
  val memTotal = (ByteConverter.ByteToMB(Runtime.getRuntime().totalMemory()))
  val memFree = (ByteConverter.ByteToMB(Runtime.getRuntime().freeMemory()))
  val memUsed = memTotal - memFree
  val memMax = (ByteConverter.ByteToMB(Runtime.getRuntime().maxMemory()))
}