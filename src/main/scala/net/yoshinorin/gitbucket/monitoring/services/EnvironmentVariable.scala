package net.yoshinorin.gitbucket.monitoring.services

import scala.collection.JavaConverters._

object EnvironmentVariable {

  def getVariables: Map[String, String] = {
    System.getenv().asScala.toMap
  }

}
