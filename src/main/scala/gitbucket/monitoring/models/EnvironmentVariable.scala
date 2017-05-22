package gitbucket.monitoring.models

import scala.collection.JavaConversions._
import scala.sys.process._
import gitbucket.monitoring.utils._

object EnvironmentVariable {
  def valiables: Either[String, Map[String, String]] = {
    try {
      Right(System.getenv().toMap)
    } catch {
      case e: Exception => Left(Message.error)
    }
  }
}
