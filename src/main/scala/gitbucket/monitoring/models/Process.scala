package gitbucket.monitoring.models

case class Tasks(
  total: String,
  running: String,
  sleeping: String,
  stopped: String,
  zombie: String
)

case class LoadAverage(
  oneMin: String,
  fiveMin: String,
  fifteenMin: String
)
