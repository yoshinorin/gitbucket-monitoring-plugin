package gitbucket.monitoring.models

class MachineResources extends OperatingSystem() {
  def core = Runtime.getRuntime().availableProcessors()
}