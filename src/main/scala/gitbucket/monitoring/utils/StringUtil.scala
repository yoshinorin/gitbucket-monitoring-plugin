package gitbucket.monitoring.utils

object StringUtil {
  def DropAndToArray(input: String, dropFirstChar: String, splitPattern:String): Array[String] = {
    input.drop(input.indexOf(dropFirstChar) + dropFirstChar.length).trim().split(splitPattern)
  }
}
