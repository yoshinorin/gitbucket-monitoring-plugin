package gitbucket.monitoring.models

import java.nio.file.{Files, Paths}

import scala.sys.process._
import gitbucket.monitoring.utils._

class GitBucketLog extends GitBucketLogBase {
  val instance = OperatingSystem.osType match {
    case OperatingSystem.Linux => new GitBucketLogBase with LinuxGitBucketLog
    case OperatingSystem.Mac => new GitBucketLogBase with MacGitBucketLog
    case OperatingSystem.Windows => new GitBucketLogBase with WindowsGitBucketLog
    case _ => new GitBucketLogBase with OtherGitBucketLog
  }
}

trait GitBucketLogBase {
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

trait LinuxGitBucketLog extends GitBucketLogBase {

}

trait MacGitBucketLog extends GitBucketLogBase {

}

trait WindowsGitBucketLog extends GitBucketLogBase {
  override def getLog(lines: Int = getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}

trait OtherGitBucketLog extends GitBucketLogBase {
  override def getLog(lines: Int = getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
    Left(OperatingSystem.notSupportedMessage)
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