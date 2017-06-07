package tutorial.webapp


import org.scalajs._
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobal, ScalaJSDefined}
import scala.scalajs.js

/**
  * Created by henrik on 19/05/17.
  */
@JSExportTopLevel("Util")
object Util {
  val url: String = "http://nixme.ddns.net:9000"
  val userUrl: String = url + "/user"
  val postUrl: String = url + "/posts"
  val authUrl: String = url + "/auth"
  val commentUrl: String = url + "/comments"
  val messageUrl: String = url + "/message"
  val upvoteUrl: String = url + "/upvote"
  val downvoteUrl: String = url + "/downvote"

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
}