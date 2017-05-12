package gitbucket.monitoring.utils

import java.util.concurrent.TimeUnit

object UnitConverter {
  def byteToKB(byte: BigDecimal, digit: Int = 2): BigDecimal = Rounding.ceil(byte / 1024)
  def byteToMB(byte: BigDecimal, digit: Int = 2): BigDecimal = Rounding.ceil(byte / (1024 * 1024))
  def byteToGB(byte: BigDecimal, digit: Int = 2): BigDecimal = Rounding.ceil(byte / (1024 * 1024 * 1024))
}

object Rounding {
  def ceil(num:BigDecimal, digit: Int = 2): BigDecimal = {
    num.setScale(digit, scala.math.BigDecimal.RoundingMode.CEILING)
  }
}

object Time {
  case class DateTime(
    days: Int,
    hours: Int,
    minutes: Int
  )

  def secondsToDateTime(seconds: Int): Either[String, DateTime] = {
    try {
      Right(DateTime(
        (seconds / (60 * 60 * 24)) % 60,
        ((seconds / (60 * 60)) % 24) % 60,
        (seconds / 60) % 60
      ))
    } catch {
      case e: Exception => Left("ERROR")
    }
  }
}