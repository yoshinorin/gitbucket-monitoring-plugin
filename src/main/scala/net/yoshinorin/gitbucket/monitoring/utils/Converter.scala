package net.yoshinorin.gitbucket.monitoring.utils

object Converter {

  implicit class byteConverter(byte: Long) {
    def byteToKB: Long = byte / 1024
    def byteToMB: Long = byte / (1024 * 1024)
    def byteToGB: Long = byte / (1024 * 1024 * 1024)
  }

  implicit class toDateTimeConverter(seconds: Int) {
    def secondsToDateTime: DateTime = {
      DateTime(
        seconds / (60 * 60 * 24),
        ((seconds / (60 * 60)) % 24) % 60,
        (seconds / 60) % 60
      )
    }
  }

  implicit class dropAndToArray(input: String) {
    def dropAndToArray(dropFirstChar: String, splitPattern: String): Array[String] = {
      input.drop(input.indexOf(dropFirstChar) + dropFirstChar.length).trim().split(splitPattern)
    }
  }

}

object Rounding {
  def ceil(num: BigDecimal, digit: Int = 2): BigDecimal = {
    num.setScale(digit, scala.math.BigDecimal.RoundingMode.CEILING)
  }
}

case class DateTime(
  days: Int,
  hours: Int,
  minutes: Int
)
