package gitbucket.monitoring.models

import java.nio.file.{Files, Paths}

import scala.sys.process._
import gitbucket.monitoring.models.{OperatingSystem, LogBack}
import gitbucket.monitoring.utils._

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
      Left("Disable log setting")
    }
  }
}

class GitBucketLog extends GitBucketLogBase {
  val instance = OperatingSystem.osType match {
    case OperatingSystem.Linux => new GitBucketLogBase with Linux
    case OperatingSystem.Mac => new GitBucketLogBase with Mac
    case OperatingSystem.Windows => new GitBucketLogBase with Windows
    case _ => new GitBucketLogBase with Other
  }

  trait Linux extends GitBucketLogBase {

  }

  trait Mac extends GitBucketLogBase {

  }

  trait Windows extends GitBucketLogBase {
    override def getLog(lines: Int = getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
      if (getDefaultSettings.logBackInfo.enableLogging) {
        getDefaultSettings.logBackInfo.logFilePath match {
          case Left(message) => Left(Message.notFound)
          case Right(p) => {
            try {
              Right(Log(
                Process(s"powershell -Command Get-Content -Path $p -Tail $lines") !!,
                lines
              ))
            } catch {
              case e: Exception => Left(Message.error)
            }
          }
        }
      } else {
        Left("Disable log setting")
      }
    }
  }

  trait Other extends GitBucketLogBase {
    override def getLog(lines: Int = getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
      Left(Message.notSupported)
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