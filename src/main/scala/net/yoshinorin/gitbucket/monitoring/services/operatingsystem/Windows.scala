package net.yoshinorin.gitbucket.monitoring.services.operatingsystem

import scala.util.Try
import scala.sys.process.Process
import net.yoshinorin.gitbucket.monitoring.models._
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.utils._

class Windows extends SystemInformation with MachineResources with ProcessInfo {

  override def getUpTime: Try[Option[UpTime]] = Try {
    Some(
      UpTime(
        Process(
          "powershell -Command \"&{$os=Get-WmiObject win32_operatingsystem;$time=((Get-Date) - $os.ConvertToDateTime($os.lastbootuptime)); $time.Days.ToString() + \\\" days \\\" +  $time.Hours.ToString() + \\\" hours \\\" + $time.Minutes.ToString() + \\\" minutes \\\"}\"").!!,
        Process("powershell -Command [Management.ManagementDateTimeConverter]::ToDateTime((Get-WmiObject Win32_OperatingSystem).LastBootUpTime)").!!
      )
    )
  }

  override def getCpu: Try[Option[Cpu]] = Try {
    Some(
      Cpu(
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        Process("powershell -Command Get-WmiObject Win32_PerfFormattedData_PerfOS_Processor | Where-Object {$_.Name -eq '_Total'} | %{ $_.PercentProcessorTime }").!!
      )
    )
  }

  override def getMemory: Try[Option[Memory]] = Try {
    val totalMem: Double = Process("powershell -Command Get-WmiObject -Class Win32_PhysicalMemory | %{ $_.Capacity} | Measure-Object -Sum | %{ ($_.sum /1024/1024) }").!!.toDouble
    val availableMem: Double = Process("powershell -Command Get-WmiObject -Class Win32_PerfFormattedData_PerfOS_Memory | %{ $_.AvailableMBytes}").!!.toDouble

    Some(
      Memory(
        totalMem.toString,
        (totalMem - availableMem).toString,
        Error.NOTSUPPORTED.message,
        Error.NOTSUPPORTED.message,
        Error.NOTSUPPORTED.message,
        availableMem.toString
      ))
  }

  override def getSwap: Try[Option[Swap]] = Try {
    None
  }

  override def getTasks: Try[Option[Tasks]] = Try {
    None
  }

  override def getLoadAverage: Try[Option[LoadAverage]] = Try {
    Some(
      LoadAverage(
        Process("powershell -Command Get-WmiObject win32_processor | Measure-Object -property LoadPercentage -Average | Select Average | %{ $_.Average }").!!,
        Error.NOTSUPPORTED.message,
        Error.NOTSUPPORTED.message
      )
    )
  }

}
