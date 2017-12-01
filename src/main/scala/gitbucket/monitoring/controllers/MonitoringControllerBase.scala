package gitbucket.monitoring.controllers

import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator

trait MonitoringControllerBase extends ControllerBase with AdminAuthenticator{
  val os = gitbucket.monitoring.services.operatingsystem.OperatingSystem.getInstance
}