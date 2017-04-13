package gitbucket.monitoring.models

class JVM () {
  val vmName = System.getProperty("java.vm.name")
  val runtimeVersion = System.getProperty("java.runtime.version")
  val memTotal = (Runtime.getRuntime().totalMemory() / (1024 * 1024))
  val memFree = (Runtime.getRuntime().freeMemory() / (1024 * 1024))
  val memUsed = memTotal - memFree
  val memMax = (Runtime.getRuntime().maxMemory() / (1024 * 1024))
}