package gitbucket.monitoring.models

import java.nio.file._
import gitbucket.monitoring.utils.{ByteConverter}

class MachineResources extends OperatingSystem() {
  def fileStore = Files.getFileStore(Paths.get("."))
  def core = Runtime.getRuntime().availableProcessors()
  def totalSpace = ByteConverter.ByteToGB(fileStore.getTotalSpace())
  def freeSpace = ByteConverter.ByteToGB(fileStore.getUnallocatedSpace())
  def usedSpace = totalSpace - freeSpace
}