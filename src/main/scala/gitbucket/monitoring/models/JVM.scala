package gitbucket.monitoring.models

import gitbucket.monitoring.utils._

object JVM {
  def vmName = System.getProperty("java.vm.name")
  def javaVersion = System.getProperty("java.version")
}

class JVM () {
  val vmName = JVM.vmName
  val javaVersion = JVM.javaVersion
  val memTotal = (Converter.byteToMB(Runtime.getRuntime().totalMemory()))
  val memFree = (Converter.byteToMB(Runtime.getRuntime().freeMemory()))
  val memUsed = memTotal - memFree
  val memMax = (Converter.byteToMB(Runtime.getRuntime().maxMemory()))
}