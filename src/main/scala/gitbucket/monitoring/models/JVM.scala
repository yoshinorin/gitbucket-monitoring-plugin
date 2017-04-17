package gitbucket.monitoring.models

import gitbucket.monitoring.utils.{ByteConverter}

class JVM () {
  val vmName = System.getProperty("java.vm.name")
  val javaVersion = System.getProperty("java.version")
  val memTotal = (ByteConverter.ByteToMB(Runtime.getRuntime().totalMemory()))
  val memFree = (ByteConverter.ByteToMB(Runtime.getRuntime().freeMemory()))
  val memUsed = memTotal - memFree
  val memMax = (ByteConverter.ByteToMB(Runtime.getRuntime().maxMemory()))
}