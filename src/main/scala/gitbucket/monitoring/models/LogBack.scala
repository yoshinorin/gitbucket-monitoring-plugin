package gitbucket.monitoring.models

case class LogBackInfo(
  enableLogging: Boolean,
  confPath: String,
  logFilePath: Either[String, String]
)
