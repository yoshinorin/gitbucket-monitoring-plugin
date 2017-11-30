package gitbucket.monitoring.services

import scala.collection.JavaConversions._
import gitbucket.monitoring.utils.Message

object EnvironmentVariable {
  def valiables: Map[String, String] = {
    System.getenv().toMap
  }
}
