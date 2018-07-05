package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import net.yoshinorin.gitbucket.monitoring.models._
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.utils._

class Other extends SystemInformation with MachineResources with ProcessInfo {

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

}
