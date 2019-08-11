package net.yoshinorin.gitbucket.monitoring.controllers

import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success}
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import net.yoshinorin.gitbucket.monitoring.services._
import net.yoshinorin.gitbucket.monitoring.information._
import net.yoshinorin.gitbucket.monitoring.models.{Cpu, LoadAverage, Memory, Swap, Tasks}
import net.yoshinorin.gitbucket.monitoring.utils.Error

class MonitoringController extends ControllerBase with AdminAuthenticator {

  private val instance = net.yoshinorin.gitbucket.monitoring.services.operatingsystem.OperatingSystem.getInstance

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation")
  })

  get("/admin/monitoring/systeminformation")(adminOnly {

    val upTime: Either[Error, UpTime] = instance.getUpTime match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left(Error.NOTSUPPORTED)
        }
      case Failure(f) => Left(Error.FAILURE)
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

    val cpu: Either[Error, Cpu] = instance.getCpu match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left(Error.NOTSUPPORTED)
        }
      case Failure(f) => Left(Error.FAILURE)
    }

    val swap: Either[Error, Swap] = instance.getSwap match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left(Error.NOTSUPPORTED)
        }
      case Failure(f) => Left(Error.FAILURE)
    }

    val memory: Either[Error, Memory] = instance.getMemory match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left(Error.NOTSUPPORTED)
        }
      case Failure(f) => Left(Error.FAILURE)
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

    val tasks: Either[Error, Tasks] = instance.getTasks match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left(Error.NOTSUPPORTED)
        }
      case Failure(f) => Left(Error.FAILURE)
    }

    val loadAve: Either[Error, LoadAverage] = instance.getLoadAverage match {
      case Success(s) =>
        s match {
          case Some(s) => Right(s)
          case None => Left(Error.NOTSUPPORTED)
        }
      case Failure(f) => Left(Error.FAILURE)
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
