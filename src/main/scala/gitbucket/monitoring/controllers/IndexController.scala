package gitbucket.monitoring.controllers

import gitbucket.core.util.AdminAuthenticator
import gitbucket.monitoring.models.{SystemInformation, EnvironmentVariable ,MachineResources, Process, Java}

class IndexController extends MonitoringControllerBase with LogController {

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation");
  })

  val sysInfo = new SystemInformation

  get("/admin/monitoring/systeminformation")(adminOnly {
    gitbucket.monitoring.information.html.system(
      sysInfo.instance.timeZone.toString,
      sysInfo.instance.nowTime.toString,
      sysInfo.instance.zoneOffset.toString,
      sysInfo.instance.dayOfWeek.toString,
      sysInfo.instance.onDocker,
      sysInfo.instance.getUpTime
      );
  })

  get("/admin/monitoring/environmentvaliable")(adminOnly {
    gitbucket.monitoring.information.html.environmentValiable(EnvironmentVariable.valiables);
  })

  get("/admin/monitoring/java")(adminOnly {
    redirect(s"/admin/monitoring/java/systemproperties");
  })

  get("/admin/monitoring/java/systemproperties")(adminOnly {
    gitbucket.monitoring.information.java.html.systemproperties(Java.getSystemProperties);
  })

  get("/admin/monitoring/java/memory")(adminOnly {
    gitbucket.monitoring.information.java.html.memory(Java.getMemoryInfo);
  })

  val machineResources = new MachineResources

  get("/admin/monitoring/machineresources")(adminOnly {
    gitbucket.monitoring.information.html.resources(
      machineResources.instance.cpuCore,
      machineResources.instance.getCpu,
      machineResources.instance.getMemory,
      machineResources.instance.getSwap,
      machineResources.instance.getDiskSpace
    );
  })

  val process = new Process

  get("/admin/monitoring/process")(adminOnly {
    gitbucket.monitoring.information.html.process(
      process.instance.getTasks,
      process.instance.getLoadAverage
    );
  })

  get("/admin/monitoring/logs")(adminOnly {
    redirect(s"/admin/monitoring/logs/logback");
  })
}