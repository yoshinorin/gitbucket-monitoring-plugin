package net.yoshinorin.gitbucket.monitoring.models

case class Cpu(
  us: String,
  sy: String,
  ni: String,
  id: String,
  wa: String,
  hi: String,
  si: String,
  st: String,
  usage: String
)

case class Memory(
  total: String,
  used: String,
  free: String,
  shared: String,
  buffCache: String,
  available: String
)

case class Swap(
  total: String,
  used: String,
  free: String
)

case class DiskSpace(
  totalSpace: String,
  freeSpace: String,
  usedSpace: String
)
