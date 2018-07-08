import gitbucket.core.controller.Context
import gitbucket.core.plugin._
import net.yoshinorin.gitbucket.monitoring.controllers.MonitoringController

import io.github.gitbucket.solidbase.model.Version

class Plugin extends gitbucket.core.plugin.Plugin {
  override val pluginId: String = "monitoring"
  override val pluginName: String = "Monitoring Plugin"
  override val description: String = "Display machine information, resources, processes and Java information."
  override val versions: List[Version] = List(
    new Version("1.0.0"),
    new Version("1.1.0"),
    new Version("1.2.0"),
    new Version("2.0.0"),
    new Version("3.0.0")
  )

  override val systemSettingMenus: Seq[(Context) => Option[Link]] = Seq(
    (context: Context) => Some(Link("monitoring", "Monitoring", "admin/monitoring", Some("server")))
  )

  override val controllers = Seq(
    "/*" -> new MonitoringController
  )
}
