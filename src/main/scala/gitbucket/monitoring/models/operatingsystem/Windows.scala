package gitbucket.monitoring.models.operatingsystem

import java.util._
import java.time._
import java.nio.file.{Paths, Files}
import scala.sys.process._
import gitbucket.monitoring.models._
import gitbucket.monitoring.utils._

class Windows extends SystemInformation with MachineResources with ProcessInfo with GitBucketLog {
  override def getUpTime: Either[String, UpTime] = {
    try {
      Right(UpTime(
        (Process("powershell -Command \"&{$os=Get-WmiObject win32_operatingsystem;$time=((Get-Date) - $os.ConvertToDateTime($os.lastbootuptime)); $time.Days.ToString() + \\\" days \\\" +  $time.Hours.ToString() + \\\" hours \\\" + $time.Minutes.ToString() + \\\" minutes \\\"}\"") !!),
        (Process("powershell -Command [Management.ManagementDateTimeConverter]::ToDateTime((Get-WmiObject Win32_OperatingSystem).LastBootUpTime)") !!)
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }

  override def getCpu: Either[String, Cpu] = {
    try {
      Right(Cpu(
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        (Process("powershell -Command Get-WmiObject Win32_PerfFormattedData_PerfOS_Processor | Where-Object {$_.Name -eq '_Total'} | %{ $_.PercentProcessorTime }") !!).toString
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }

  override def getMemory: Either[String, Memory] = {
    try {
      val totalMem = (Process("powershell -Command Get-WmiObject -Class Win32_PhysicalMemory | %{ $_.Capacity} | Measure-Object -Sum | %{ ($_.sum /1024/1024) }") !!).toDouble
      val availableMem = (Process("powershell -Command Get-WmiObject -Class Win32_PerfFormattedData_PerfOS_Memory | %{ $_.AvailableMBytes}") !!).toDouble
      Right(Memory(
        totalMem.toString,
        (totalMem - availableMem).toString,
        Message.notSupported,
        Message.notSupported,
        //(Process("powershell -Command Get-WmiObject -Class Win32_PerfFormattedData_PerfOS_Memory | %{ $_.CacheBytes /1024/1024 }") !!),
        Message.notSupported,
        availableMem.toString
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }

  override def getSwap: Either[String, Swap] = {
    Left(Message.notSupported)
  }

  override def getTasks: Either[String, Tasks] = {
    Left(Message.notSupported)
  }

  override def getLoadAverage: Either[String, LoadAverage] = {
    try {
      Right(LoadAverage(
        (Process("powershell -Command Get-WmiObject win32_processor | Measure-Object -property LoadPercentage -Average | Select Average | %{ $_.Average }") !!).toString,
        Message.notSupported,
        Message.notSupported
      ))
    } catch {
      case e: Exception => Left(Message.error)
    }
  }

  override def getLog(lines: Int = GitBucketLog.getDefaultSettings.defaultDisplayLines): Either[String, Log] = {
    if (GitBucketLog.getDefaultSettings.logBackInfo.enableLogging) {
      GitBucketLog.getDefaultSettings.logBackInfo.logFilePath match {
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
      Left(GitBucketLog.disableMessage)
    }
  }
}