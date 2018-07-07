package net.yoshinorin.gitbucket.monitoring.controllers

import scala.collection.JavaConverters._
import scala.util.{Failure, Success}
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.information._
import net.yoshinorin.gitbucket.monitoring.models.{Cpu, LoadAverage, Memory, Swap, Tasks}

class MonitoringController extends ControllerBase with AdminAuthenticator {

  private val instance = net.yoshinorin.gitbucket.monitoring.services.operatingsystem.OperatingSystem.getInstance

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation")
  })

  get("/admin/monitoring/systeminformation")(adminOnly {

    val upTime: Either[String, UpTime] = instance.getUpTime match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    html.system(
      instance.timeZone.toString,
      instance.getCurrentTime.toString,
      instance.getZoneOffset.toString,
      instance.getDayOfWeek.toString,
      instance.onDocker,
      upTime
    )

  })

  get("/admin/monitoring/environmentvariable")(adminOnly {
    html.environmentVariable(System.getenv().asScala.toMap)
  })

  get("/admin/monitoring/machineresources")(adminOnly {

    val cpu: Either[String, Cpu] = instance.getCpu match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    val swap: Either[String, Swap] = instance.getSwap match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    val memory: Either[String, Memory] = instance.getMemory match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    html.resources(
      instance.cpuCore,
      cpu,
      memory,
      swap,
      instance.getDiskSpace
    )
  })

  get("/admin/monitoring/process")(adminOnly {

    val tasks: Either[String, Tasks] = instance.getTasks match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left("NOT SUPPORT")
        }
      case Failure(f) => Left("ERROR")
    }

    val loadAve: Either[String, LoadAverage] = instance.getLoadAverage match {
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
