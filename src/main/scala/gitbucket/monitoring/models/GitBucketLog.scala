package gitbucket.monitoring.models.gitbucketLog

import java.nio.file.{Files, Paths}

import scala.sys.process._
import gitbucket.monitoring.models.{OperatingSystem, LogBack}
import gitbucket.monitoring.utils._

case class DefaultSettings (
  logBackInfo: LogBack.LogBackInfo,
  defaultDisplayLines: Int,
  displayLimitLines: Int
)

case class Log (
  log: String,
  displayedLines: Int
)

trait GitBucketLog {
  def getDefaultSettings: DefaultSettings = {
    DefaultSettings(
      LogBack.getLogBackInfo,
      1000,
      30000
    )
  }
  def getLog(lines: Int = getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
    if (getDefaultSettings.logBackInfo.enableLogging) {
      getDefaultSettings.logBackInfo.logFilePath match {
        case Left(message) => Left("ERROR : Not found")
        case Right(p) => {
          try {
            Right(Log(
                Process(s"tail -n $lines $p") !!,
                lines
              ))
          } catch {
            case e: Exception => Left("ERROR")
          }
        }
      }
    } else {
      Left("Disable log setting")
    }
  }
}

trait Action {
  self: GitBucketLog =>
    def getLog(lines: Int = getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
      getLog()
    }
}

trait Linux extends GitBucketLog {

}

trait Mac extends GitBucketLog {

}

trait Windows extends GitBucketLog {
  override def getLog(lines: Int = getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}

trait Other extends GitBucketLog {
  override def getLog(lines: Int = getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}