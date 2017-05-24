package gitbucket.monitoring.controllers

import gitbucket.monitoring.models.{LogBack, GitBucketLog}
import gitbucket.monitoring.information.logs._

trait LogController extends MonitoringControllerBase {

  get("/admin/monitoring/logs/logback")(adminOnly {
    gitbucket.monitoring.information.logs.html.logback(
      LogBack.getLogBackInfo,
      LogBack.getLogBackSettings
    );
  })

  get("/admin/monitoring/logs/gitbucketlog")(adminOnly {
    val defaultSettings = GitBucketLog.getDefaultSettings
    val lineNum = request.getParameter("line-num")

    if (lineNum != null){
      try {
        val n = lineNum.toInt
        if (n > defaultSettings.displayLimitLines) {
          html.gitbucketlog(defaultSettings, os.getLog(defaultSettings.displayLimitLines));
        } else {
          html.gitbucketlog(defaultSettings, os.getLog(n));
        }
      } catch {
        case e: Exception => html.gitbucketlog(defaultSettings, os.getLog());
      }
    } else {
      html.gitbucketlog(defaultSettings, os.getLog());
    }
  })
}