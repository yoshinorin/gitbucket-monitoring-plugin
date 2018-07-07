package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import scala.util.Try
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.models.{Cpu, Tasks}

class Mac extends SystemInformation with MachineResources with ProcessInfo {

  override def getTasks: Try[Option[Tasks]] = Try {
    None
  }

  override def getCpu: Try[Option[Cpu]] = Try {
    None
  }

}
