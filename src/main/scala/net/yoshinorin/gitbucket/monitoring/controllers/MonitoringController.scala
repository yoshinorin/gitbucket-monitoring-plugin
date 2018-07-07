package net.yoshinorin.gitbucket.monitoring.controllers

import scala.collection.JavaConverters._
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.information._

class MonitoringController extends ControllerBase with AdminAuthenticator {

  private val os = net.yoshinorin.gitbucket.monitoring.services.operatingsystem.OperatingSystem.getInstance

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation")
  })

  get("/admin/monitoring/systeminformation")(adminOnly {
    html.system(
      os.timeZone.toString,
      os.getCurrentTime.toString,
      os.getZoneOffset.toString,
      os.getDayOfWeek.toString,
      os.onDocker,
      os.getUpTime
    )
  })

  get("/admin/monitoring/environmentvariable")(adminOnly {
    html.environmentVariable(System.getenv().asScala.toMap)
  })

  get("/admin/monitoring/machineresources")(adminOnly {
    html.resources(
      os.cpuCore,
      os.getCpu,
      os.getMemory,
      os.getSwap,
      os.getDiskSpace
    )
  })

  get("/admin/monitoring/process")(adminOnly {
    html.process(
      os.getTasks,
      os.getLoadAverage
    )

  })

  get("/admin/monitoring/java")(adminOnly {
    redirect(s"/admin/monitoring/java/systemproperties")
  })

  get("/admin/monitoring/java/systemproperties")(adminOnly {
    java.html.systemproperties(Java.getSystemProperties)
  })

  get("/admin/monitoring/java/memory")(adminOnly {
    java.html.memory(Java.getMemoryInfo)
  })

}
