package gitbucket.monitoring.services.operatingsystem

import java.util._
import java.time._
import java.nio.file.{Paths, Files}
import scala.sys.process._
import gitbucket.monitoring.models._
import gitbucket.monitoring.services._
import gitbucket.monitoring.utils._

class Other extends SystemInformation with MachineResources with ProcessInfo with GitBucketLog {
  override def getUpTime: Either[String, UpTime] = {
    Left(Message.notSupported)
  }

  override def getCpu: Either[String, Cpu] = {
    Left(Message.notSupported)
  }

  override def getMemory: Either[String, Memory] = {
    Left(Message.notSupported)
  }

  override def getSwap: Either[String, Swap] = {
    Left(Message.notSupported)
  }

  override def getTasks: Either[String, Tasks] = {
    Left(Message.notSupported)
  }

  override def getLoadAverage: Either[String, LoadAverage] = {
    Left(Message.notSupported)
  }

  override def getLog(lines: Int = GitBucketLog.getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
    Left(Message.notSupported)
  }
}