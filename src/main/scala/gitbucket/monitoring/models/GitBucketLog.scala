package gitbucket.monitoring.models.gitbucketLog

import java.nio.file.{Files, Paths}

import scala.sys.process._
import gitbucket.monitoring.models.{OperatingSystem, LogBack}
import gitbucket.monitoring.utils._

case class Info(
  logBackInfo: LogBack.LogBackInfo,
  defaultNum: Int,
  desplayLimit: Int,
  inputtedNum: Int,
  log: String
)

object GitBucketLog {
  val defaultNum = 1000
  val desplayLimit = 30000
}

trait GitBucketLog {
  val logBackInfo = LogBack.getLogBackInfo
  def getLog(lines: Int = GitBucketLog.defaultNum): Either[String, Info] = {
    if (logBackInfo.enableLogging) {
      logBackInfo.logfilePath match {
        case Left(message) => Left("ERROR : Not found")
        case Right(p) => {
          try {
            Right(
              Info(
                logBackInfo,
                GitBucketLog.defaultNum,
                GitBucketLog.desplayLimit,
                lines,
                (Process(s"tail -n $lines $p") !!)
              )
            )
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
    def getLog(lines: Int = GitBucketLog.defaultNum): Either[String, Info] = {
      getLog()
    }
}

trait Linux extends GitBucketLog {

}

trait Mac extends GitBucketLog {

}

trait Windows extends GitBucketLog {
  override def getLog(lines: Int = GitBucketLog.defaultNum): Either[String, Info] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}

trait Other extends GitBucketLog {
  override def getLog(lines: Int = GitBucketLog.defaultNum): Either[String, Info] = {
    Left(OperatingSystem.notSupportedMessage)
  }
}