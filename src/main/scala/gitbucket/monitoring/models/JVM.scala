package gitbucket.monitoring.models

case class JVM (vmName: String, runtimeVersion: String, memTotal: Long, memFree: Long, memUsed: Long, memMax: Long)