package gitbucket.monitoring.models

import java.nio.file.{Files, Paths}
import scala.sys.process._
import gitbucket.monitoring.utils._

object GitBucketLog {
  val disableMessage = "Log setting is disable."

  def getDefaultSettings: DefaultSettings = {
    DefaultSettings(
      LogBack.getLogBackInfo,
      1000,
      30000
    )
  }
}

trait GitBucketLog {
  def getLog(lines: Int = GitBucketLog.getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
    if (GitBucketLog.getDefaultSettings.logBackInfo.enableLogging) {
      GitBucketLog.getDefaultSettings.logBackInfo.logFilePath match {
        case Left(message) => Left(Message.notFound)
        case Right(p) => {
          try {
            Right(Log(
              Process(s"tail -n $lines $p") !!,
              lines
            ))
          } catch {
            case e: Exception => Left(Message.error)
          }
        }
      }
    } else {
      Left(GitBucketLog.disableMessage)
    }
  }
}

case class DefaultSettings (
  logBackInfo: LogBack.LogBackInfo,
  defaultDisplayLines: Int,
  displayLimitLines: Int
)

case class Log (
  log: String,
  displayedLines: Int
)