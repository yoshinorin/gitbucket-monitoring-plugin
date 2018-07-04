package net.yoshinorin.gitbucket.monitoring.services

import java.io.IOException
import scala.sys.process._
import net.yoshinorin.gitbucket.monitoring.models.{DefaultSettings, Log}
import net.yoshinorin.gitbucket.monitoring.utils._

object GitBucketLog {
  val disableMessage = "Log setting is disable."

  def getDefaultSettings: DefaultSettings = {
    DefaultSettings(
      LogBack.getLogBackSettings,
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
            Right(
              Log(
                Process(s"tail -n $lines $p") !!,
                lines
              ))
          } catch {
            case e: IOException => Left(Message.error)
          }
        }
      }
    } else {
      Left(GitBucketLog.disableMessage)
    }
  }
}
