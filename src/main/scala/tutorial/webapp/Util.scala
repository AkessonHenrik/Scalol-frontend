package tutorial.webapp


import org.scalajs._
import org.scalajs.jquery.jQuery

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobal, ScalaJSDefined}
import scala.scalajs.js
import scala.concurrent.ExecutionContext.Implicits.global._
import org.scalajs.dom
import org.scalajs.jquery.jQuery
import tutorial.webapp.Login.usernameElement
import tutorial.webapp.Main.{loggedIn, parse}

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global._
import org.scalajs._
import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.util.{Failure, Success, Try}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js.JSON

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

  def jsonAndTokenHeaderMap = Map("Content-Type" -> "application/json", "auth" -> dom.window.localStorage.getItem("scalol_token"))

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

  def get(url: String, data: js.Any, headers: Map[String, String], callback: dom.XMLHttpRequest => Unit): Unit = {
    println("get " + url)
    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET",
      url
    )
    if (headers != null)
      for (header <- headers) {
        xhr.setRequestHeader(header._1, header._2)
      }
    xhr.onload = { (e: dom.Event) =>
      if (xhr.status == 200) {
        callback(xhr)
      }
    }
    xhr.send()
  }

  def post(url: String, data: js.Any, headers: Map[String, String], callback: dom.XMLHttpRequest => Unit) {
    val xhr = new dom.XMLHttpRequest()
    xhr.open("POST",
      url
    )
    for (header <- headers) {
      xhr.setRequestHeader(header._1, header._2)
    }
    xhr.onload = { (e: dom.Event) =>
      callback(xhr)
    }
    xhr.send(data)
  }

  def patch(url: String, data: scala.scalajs.js.Any, headers: Map[String, String], callback: dom.XMLHttpRequest => Unit) {
    val xhr = new dom.XMLHttpRequest()
    xhr.open("PATCH",
      url
    )
    for ((key, value) <- headers) {
      xhr.setRequestHeader(key, value)
    }
    xhr.onload = { (e: dom.Event) =>
      callback(xhr)
    }
    xhr.send(data)
  }

}