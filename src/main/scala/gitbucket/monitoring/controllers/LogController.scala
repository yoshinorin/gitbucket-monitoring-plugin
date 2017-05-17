package gitbucket.monitoring.controllers

import gitbucket.monitoring.models.{LogBack, GitBucketLog}

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
          gitbucket.monitoring.information.logs.html.gitbucketlog(defaultSettings, gitbucketLog.getLog(defaultSettings.displayLimitLines));
        } else {
          gitbucket.monitoring.information.logs.html.gitbucketlog(defaultSettings, gitbucketLog.getLog(n));
        }
      } catch {
        case e: Exception => gitbucket.monitoring.information.logs.html.gitbucketlog(defaultSettings, gitbucketLog.getLog());
      }
    } else {
      gitbucket.monitoring.information.logs.html.gitbucketlog(defaultSettings, gitbucketLog.getLog());
    }
  })
}