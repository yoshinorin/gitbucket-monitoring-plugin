package gitbucket.monitoring.utils

object ByteConverter {
  def ByteToKB(byte: Long): Long = byte / 1024
  def ByteToMB(byte: Long): Long = byte / (1024 * 1024)
  def ByteToGB(byte: Long): Long = byte / (1024 * 1024 * 1024)
}