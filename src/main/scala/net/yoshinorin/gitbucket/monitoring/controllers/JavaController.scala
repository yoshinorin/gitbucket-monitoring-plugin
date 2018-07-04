package net.yoshinorin.gitbucket.monitoring.controllers

import net.yoshinorin.gitbucket.monitoring.services.Java
import net.yoshinorin.gitbucket.monitoring.information.java._

trait JavaController extends MonitoringControllerBase {

  get("/admin/monitoring/java")(adminOnly {
    redirect(s"/admin/monitoring/java/systemproperties");
  })

  get("/admin/monitoring/java/systemproperties")(adminOnly {
    html.systemproperties(Java.getSystemProperties);
  })

  get("/admin/monitoring/java/memory")(adminOnly {
    html.memory(Java.getMemoryInfo);
  })
}
