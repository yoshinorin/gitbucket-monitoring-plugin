package gitbucket.monitoring.models

class OperatingSystem {
  def osName = System.getProperty("os.name")
  def osVersion = System.getProperty("os.version")
  def osArch = System.getProperty("os.arch")
  def isLinux = if (osName.toLowerCase == "linux") true else false
}