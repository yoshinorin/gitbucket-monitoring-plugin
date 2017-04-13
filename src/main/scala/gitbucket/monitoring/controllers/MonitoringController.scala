package gitbucket.monitoring.controllers

import io.github.gitbucket.scalatra.forms._
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import gitbucket.core.view.helpers._

import gitbucket.monitoring.information.html._
import gitbucket.monitoring.models._

class MonitoringController extends ControllerBase with AdminAuthenticator {

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation");
  })

  get("/admin/monitoring/systeminformation")(adminOnly {
    gitbucket.monitoring.information.html.system(new SystemInformation());
  })

  get("/admin/monitoring/jvm")(adminOnly {
    gitbucket.monitoring.information.html.jvm(new JVM());
  })

  get("/admin/monitoring/machineresources")(adminOnly {
    gitbucket.monitoring.information.html.resources(new MachineResources());
  })
}