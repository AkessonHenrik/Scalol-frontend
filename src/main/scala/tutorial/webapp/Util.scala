package tutorial.webapp


import org.scalajs._
import org.scalajs.jquery.jQuery

import scala.scalajs.js

import scala.scalajs.js.annotation.{JSExport, JSGlobal, ScalaJSDefined}

import scala.scalajs.js

/**
  * Created by henrik on 19/05/17.
  */
object Util {
  val url: String = "http://nixme.ddns.net:9000"
  val userUrl: String = url + "/user"
  val postUrl: String = url + "/posts"
  val authUrl: String = url + "/auth"
  val commentUrl: String = url + "/comment"
  val messageUrl: String = url + "/message"
  def loadNavbar: Unit = {
    println("loadNavbar")
    jQuery("#scalolNavbar").load("./navbar.html")
  }
}