package gitbucket.monitoring.utils

object StringUtil {
  def dropAndToArray(input: String, dropFirstChar: String, splitPattern: String): Array[String] = {
    input.drop(input.indexOf(dropFirstChar) + dropFirstChar.length).trim().split(splitPattern)
  }
}
