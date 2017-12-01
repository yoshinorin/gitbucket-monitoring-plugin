package gitbucket.monitoring.controllers

import scala.util.Try
import gitbucket.monitoring.services.{LogBack, GitBucketLog}
import gitbucket.monitoring.information.logs._

trait LogController extends MonitoringControllerBase {

  get("/admin/monitoring/logs/logback")(adminOnly {
    gitbucket.monitoring.information.logs.html.logback(
      LogBack.logBackSettingsFile,
      LogBack.getLogBackSettings
    );
  })

  get("/admin/monitoring/logs/gitbucketlog")(adminOnly {
    val defaultSettings = GitBucketLog.getDefaultSettings
    val lineNum = request.getParameter("lines")

    if (Try(lineNum.toInt).toOption != None){
      val n = lineNum.toInt
      if (n > defaultSettings.displayLimitLines) {
        html.gitbucketlog(defaultSettings, os.getLog(defaultSettings.displayLimitLines));
      } else {
        html.gitbucketlog(defaultSettings, os.getLog(n));
      }
    } else {
      html.gitbucketlog(defaultSettings, os.getLog(defaultSettings.defaultDisplayLines));
    }
  })
}