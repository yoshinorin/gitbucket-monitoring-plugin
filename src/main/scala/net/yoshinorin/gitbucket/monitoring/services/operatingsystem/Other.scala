package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import scala.util.Try
import net.yoshinorin.gitbucket.monitoring.models._
import net.yoshinorin.gitbucket.monitoring.services._

class Other extends SystemInformation with MachineResources with ProcessInfo {

  override def getUpTime: Try[Option[UpTime]] = Try {
    None
  }

  override def getCpu: Try[Option[Cpu]] = Try {
    None
  }

  override def getMemory: Try[Option[Memory]] = Try {
    None
  }

  override def getSwap: Try[Option[Swap]] = Try {
    None
  }

  override def getTasks: Try[Option[Tasks]] = Try {
    None
  }

  override def getLoadAverage: Try[Option[LoadAverage]] = Try {
    None
  }

}
