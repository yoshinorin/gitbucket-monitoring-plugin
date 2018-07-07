package net.yoshinorin.gitbucket.monitoring.controllers

import scala.collection.JavaConverters._
import scala.util.{Failure, Success}
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.information._
import net.yoshinorin.gitbucket.monitoring.models.{Cpu, LoadAverage, Memory, Swap, Tasks}

class MonitoringController extends ControllerBase with AdminAuthenticator {

  private val os = net.yoshinorin.gitbucket.monitoring.services.operatingsystem.OperatingSystem.getInstance

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation")
  })

  get("/admin/monitoring/systeminformation")(adminOnly {

    val upTime: Either[String, UpTime] = os.getUpTime match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    html.system(
      os.timeZone.toString,
      os.getCurrentTime.toString,
      os.getZoneOffset.toString,
      os.getDayOfWeek.toString,
      os.onDocker,
      upTime
    )

  })

  get("/admin/monitoring/environmentvariable")(adminOnly {
    html.environmentVariable(System.getenv().asScala.toMap)
  })

  get("/admin/monitoring/machineresources")(adminOnly {

    val cpu: Either[String, Cpu] = os.getCpu match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    val swap: Either[String, Swap] = os.getSwap match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    val memory: Either[String, Memory] = os.getMemory match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    html.resources(
      os.cpuCore,
      cpu,
      memory,
      swap,
      os.getDiskSpace
    )
  })

  get("/admin/monitoring/process")(adminOnly {

    val tasks: Either[String, Tasks] = os.getTasks match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    val loadAve: Either[String, LoadAverage] = os.getLoadAverage match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    html.process(
      tasks,
      loadAve
    )

  })

  get("/admin/monitoring/java")(adminOnly {
    redirect(s"/admin/monitoring/java/systemproperties")
  })

  get("/admin/monitoring/java/systemproperties")(adminOnly {
    java.html.systemproperties(Java.getSystemProperties)
  })

  get("/admin/monitoring/java/memory")(adminOnly {
    java.html.memory(Java.getMemoryInfo)
  })

}
