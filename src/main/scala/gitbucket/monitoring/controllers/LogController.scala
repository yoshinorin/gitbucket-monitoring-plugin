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

  val gitbucketLog = new GitBucketLog

  get("/admin/monitoring/logs/gitbucketlog")(adminOnly {
    val defaultSettings = gitbucketLog.getDefaultSettings
    val lineNum = request.getParameter("line-num")

    if (lineNum != null){
      try {
        val n = lineNum.toInt
        if (n > defaultSettings.displayLimitLines) {
          html.gitbucketlog(defaultSettings, gitbucketLog.getLog(defaultSettings.displayLimitLines));
        } else {
          html.gitbucketlog(defaultSettings, gitbucketLog.getLog(n));
        }
      } catch {
        case e: Exception => html.gitbucketlog(defaultSettings, gitbucketLog.getLog());
      }
    } else {
      html.gitbucketlog(defaultSettings, gitbucketLog.getLog());
    }
  })
}