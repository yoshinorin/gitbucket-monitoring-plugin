package net.yoshinorin.gitbucket.monitoring.controllers

import gitbucket.core.util.AdminAuthenticator
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.information._

class IndexController extends MonitoringControllerBase with JavaController with LogController {

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation");
  })

  get("/admin/monitoring/systeminformation")(adminOnly {
    html.system(
      os.timeZone.toString,
      os.nowTime.toString,
      os.zoneOffset.toString,
      os.dayOfWeek.toString,
      os.onDocker,
      os.getUpTime
    );
  })

  get("/admin/monitoring/environmentvariable")(adminOnly {
    html.environmentVariable(EnvironmentVariable.variables);
  })

  get("/admin/monitoring/machineresources")(adminOnly {
    html.resources(
      os.cpuCore,
      os.getCpu,
      os.getMemory,
      os.getSwap,
      os.getDiskSpace
    );
  })

  get("/admin/monitoring/process")(adminOnly {
    html.process(
      os.getTasks,
      os.getLoadAverage
    );
  })

  get("/admin/monitoring/logs")(adminOnly {
    redirect(s"/admin/monitoring/logs/logback");
  })
}
