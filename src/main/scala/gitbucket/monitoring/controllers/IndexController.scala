package gitbucket.monitoring.controllers

import gitbucket.core.util.AdminAuthenticator
import gitbucket.monitoring.models.{SystemInformation, EnvironmentVariable ,MachineResources, Process}
import gitbucket.monitoring.information._

class IndexController extends MonitoringControllerBase with JavaController with LogController {

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation");
  })

  val sysInfo = new SystemInformation

  get("/admin/monitoring/systeminformation")(adminOnly {
    html.system(
      sysInfo.instance.timeZone.toString,
      sysInfo.instance.nowTime.toString,
      sysInfo.instance.zoneOffset.toString,
      sysInfo.instance.dayOfWeek.toString,
      sysInfo.instance.onDocker,
      sysInfo.instance.getUpTime
      );
  })

  get("/admin/monitoring/environmentvaliable")(adminOnly {
    html.environmentValiable(EnvironmentVariable.valiables);
  })

  val machineResources = new MachineResources

  get("/admin/monitoring/machineresources")(adminOnly {
    html.resources(
      machineResources.instance.cpuCore,
      machineResources.instance.getCpu,
      machineResources.instance.getMemory,
      machineResources.instance.getSwap,
      machineResources.instance.getDiskSpace
    );
  })

  val process = new Process

  get("/admin/monitoring/process")(adminOnly {
    html.process(
      process.instance.getTasks,
      process.instance.getLoadAverage
    );
  })

  get("/admin/monitoring/logs")(adminOnly {
    redirect(s"/admin/monitoring/logs/logback");
  })
}