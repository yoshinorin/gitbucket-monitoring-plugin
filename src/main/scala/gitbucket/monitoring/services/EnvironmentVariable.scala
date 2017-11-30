package gitbucket.monitoring.services

import scala.collection.JavaConverters._
import gitbucket.monitoring.utils.Message

object EnvironmentVariable {
  def valiables: Map[String, String] = {
    System.getenv().asScala.toMap
  }
}
