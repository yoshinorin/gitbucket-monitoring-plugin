package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.models.{Cpu, Tasks}
import net.yoshinorin.gitbucket.monitoring.utils._

class Mac extends SystemInformation with MachineResources with ProcessInfo {

  override def getTasks: Either[String, Tasks] = {
    Left(Message.notSupported)
  }

  override def getCpu: Either[String, Cpu] = {
    Left(Message.notSupported)
  }

}
