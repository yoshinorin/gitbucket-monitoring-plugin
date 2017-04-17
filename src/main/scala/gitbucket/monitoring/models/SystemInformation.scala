package gitbucket.monitoring.models

import java.util._
import java.time._

class SystemInformation extends OperatingSystem () {
  def nowTime = LocalDateTime.now()
  def timeZone = ZoneId.systemDefault()
  def zoneOffset = timeZone.getRules().getOffset(nowTime)
  def dayOfWeek = nowTime.getDayOfWeek()
}