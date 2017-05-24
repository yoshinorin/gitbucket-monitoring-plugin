package gitbucket.monitoring.services.operatingsystem

import java.util._
import java.time._
import java.nio.file.{Paths, Files}
import scala.sys.process._
import gitbucket.monitoring.services._
import gitbucket.monitoring.models.{Cpu, Tasks}
import gitbucket.monitoring.utils._

class Mac extends SystemInformation with MachineResources with ProcessInfo with GitBucketLog {
  override def getTasks: Either[String, Tasks] = {
    Left(Message.notSupported)
  }

  override def getCpu: Either[String, Cpu] = {
    Left(Message.notSupported)
  }
}