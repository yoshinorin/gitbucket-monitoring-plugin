package gitbucket.monitoring.controllers

import io.github.gitbucket.scalatra.forms._
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import gitbucket.core.view.helpers._

import gitbucket.monitoring.html
import play.twirl.api.Html

import java.net._
import scala.collection.JavaConversions._

class MonitoringController extends ControllerBase with AdminAuthenticator {

  get("/admin/monitoring")(adminOnly {
    val osInfo = getOSInfo()
    val jvmInfo = getJVMInfo()
    html.index(None, osInfo, jvmInfo);
  })

  def getOSInfo(): OSInfo = {
    new OSInfo(System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"))
  }

  def getJVMInfo(): JVMInfo = {
    val memTotal = (Runtime.getRuntime().totalMemory() / (1024 * 1024))
    val memFree = (Runtime.getRuntime().freeMemory() / (1024 * 1024))
    val memUsed = memTotal - memFree
    val memMax = (Runtime.getRuntime().maxMemory() / (1024 * 1024))
    new JVMInfo(System.getProperty("java.vm.name"), System.getProperty("java.runtime.version"), memTotal, memFree, memUsed, memMax)
  }
}

case class OSInfo (
  osName: String,
  osVersion: String,
  osArch: String)

case class JVMInfo(
  vmName: String,
  runtimeVersion: String,
  memTotal: Long,
  memFree: Long,
  memUsed: Long,
  memMax: Long)