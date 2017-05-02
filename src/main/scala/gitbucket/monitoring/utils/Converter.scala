package gitbucket.monitoring.utils

object Converter {
  def byteToKB(byte: BigDecimal, digit: Int = 2): BigDecimal = ceil(byte / 1024)
  def byteToMB(byte: BigDecimal, digit: Int = 2): BigDecimal = ceil(byte / (1024 * 1024))
  def byteToGB(byte: BigDecimal, digit: Int = 2): BigDecimal = ceil(byte / (1024 * 1024 * 1024))

  def ceil(num:BigDecimal, digit: Int = 2): BigDecimal = {
    num.setScale(digit, scala.math.BigDecimal.RoundingMode.CEILING)
  }
}