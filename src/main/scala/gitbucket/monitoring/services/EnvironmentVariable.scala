package gitbucket.monitoring.services

import scala.collection.JavaConverters._

object EnvironmentVariable {
  def valiables: Map[String, String] = {
    System.getenv().asScala.toMap
  }
}
