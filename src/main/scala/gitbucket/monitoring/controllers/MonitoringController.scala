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
    html.index(None, osInfo);
  })

  def getOSInfo(): OSInfo = {
    new OSInfo(System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"))
  }
}

case class OSInfo (
  osName: String,
  osVersion: String,
  osArch: String)