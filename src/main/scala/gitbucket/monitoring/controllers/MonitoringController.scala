package gitbucket.monitoring.controllers

import io.github.gitbucket.scalatra.forms._
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import gitbucket.core.view.helpers._
import gitbucket.monitoring.information.html._
import gitbucket.monitoring.models._

class MonitoringController extends ControllerBase with AdminAuthenticator {
  val sysInfo = OperatingSystem.osType match {
    case OperatingSystem.Linux => new systemInformation.Info with systemInformation.Linux
    case OperatingSystem.Mac => new systemInformation.Info with systemInformation.Mac
    case OperatingSystem.Windows => new systemInformation.Action with systemInformation.Windows
    case _ => new systemInformation.Action with systemInformation.Other
  }

  val machineRes = OperatingSystem.osType match {
    case OperatingSystem.Linux => new machineResources.Resources with machineResources.Linux
    case OperatingSystem.Mac => new machineResources.Resources with machineResources.Mac
    case OperatingSystem.Windows => new machineResources.Action with machineResources.Windows
    case _ => new machineResources.Action with machineResources.Other
  }

  val processInfo = OperatingSystem.osType match {
    case OperatingSystem.Linux => new processInformation.Info with processInformation.Linux
    case OperatingSystem.Mac => new processInformation.Info with processInformation.Mac
    case OperatingSystem.Windows => new processInformation.Action with processInformation.Windows
    case _ => new processInformation.Action with processInformation.Other
  }

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation");
  })

  get("/admin/monitoring/systeminformation")(adminOnly {
    gitbucket.monitoring.information.html.system(sysInfo);
  })

  get("/admin/monitoring/environmentvaliable")(adminOnly {
    gitbucket.monitoring.information.html.environmentValiable(EnvironmentVariable.valiables);
  })

  get("/admin/monitoring/java")(adminOnly {
    gitbucket.monitoring.information.html.java(new Java());
  })

  get("/admin/monitoring/machineresources")(adminOnly {
    gitbucket.monitoring.information.html.resources(machineRes);
  })

  get("/admin/monitoring/process")(adminOnly {
    gitbucket.monitoring.information.html.process(processInfo);
  })
}