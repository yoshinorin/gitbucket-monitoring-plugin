package gitbucket.monitoring.services

import scala.collection.JavaConverters._

object EnvironmentVariable {
  def variables: Map[String, String] = {
    System.getenv().asScala.toMap
  }
}
