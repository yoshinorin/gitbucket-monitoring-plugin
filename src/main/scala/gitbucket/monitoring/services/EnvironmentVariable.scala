package gitbucket.monitoring.services

import scala.collection.JavaConversions._
import gitbucket.monitoring.utils.Message

object EnvironmentVariable {
  def valiables: Either[String, Map[String, String]] = {
    try {
      Right(System.getenv().toMap)
    } catch {
      case e: Exception => Left(Message.error)
    }
  }
}
