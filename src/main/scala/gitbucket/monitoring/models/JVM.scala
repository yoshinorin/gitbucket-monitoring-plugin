package gitbucket.monitoring.models

import gitbucket.monitoring.utils._

object JVM {
  def vmName = System.getProperty("java.vm.name")
  def javaVersion = System.getProperty("java.version")
}

class JVM () {
  val vmName = JVM.vmName
  val javaVersion = JVM.javaVersion
  val memTotal = (UnitConverter.byteToMB(Runtime.getRuntime().totalMemory()))
  val memFree = (UnitConverter.byteToMB(Runtime.getRuntime().freeMemory()))
  val memUsed = memTotal - memFree
  val memMax = (UnitConverter.byteToMB(Runtime.getRuntime().maxMemory()))
}