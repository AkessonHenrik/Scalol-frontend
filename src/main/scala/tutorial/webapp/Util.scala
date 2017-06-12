package tutorial.webapp


import org.scalajs._
import org.scalajs.jquery.jQuery

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobal, ScalaJSDefined}
import scala.scalajs.js

/**
  * Created by henrik on 19/05/17.
  */
@JSExportTopLevel("Util")
object Util {
  def url: String = "https://nixme.ddns.net"

  def userUrl: String = url + "/user"

  @JSExport
  def postUrl: String = url + "/posts"

  def authUrl: String = url + "/auth"

  def commentUrl: String = url + "/comments"

  def messageUrl: String = url + "/messages"

  def upvoteUrl: String = url + "/upvote"

  def downvoteUrl: String = url + "/downvote"

  def uploadUrl: String = url + "/upload"

  def blockUrl: String = url + "/block_user/"

  def unblockUrl: String = url + "/unblock_user/"

  @JSExport
  def loadNavbar(): Unit = {
    if (dom.window.localStorage.getItem("scalol_token") == null) {
      jQuery("#scalolNavbar").load("./navbar.html")
      jQuery("#logout").click {
        (_: JQueryEvent) => {
          println("Hello from logout")
          dom.window.localStorage.removeItem("scalol_token")
          dom.window.localStorage.removeItem("scalol_username")
          Util.loadNavbar
        }
      }
    } else {
      jQuery("#scalolNavbar").load("./loggednavbar.html")
    }
  }

  @JSExport
  def insertUsername(): Unit = {
    jQuery("#modifyProfile").append(dom.window.localStorage.getItem("scalol_username"))
  }

  def get(url: String, data: Any, headers: Map[String, String], callback: Future[dom.XMLHttpRequest] => Unit) {

  }

  def post(url: String, data: Any, headers: Map[String, String], callback: Future[dom.XMLHttpRequest] => Unit) {

  }

  def patch(url: String, data: Any, headers: Map[String, String], callback: Future[dom.XMLHttpRequest] => Unit) {

  }

}