package tutorial.webapp


import com.google.gson._
import org.scalajs._
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSExport, JSGlobal, ScalaJSDefined}
import scalaj.http._

/**
  * Created by henrik on 19/05/17.
  */
class Signup extends js.JSApp {
  lazy val emailElement = jQuery("#email")
  lazy val usernameElement = jQuery("#username")
  lazy val passwordElement = jQuery("#password")
  lazy val repeatPasswordElement = jQuery("#repeatPassword")
  lazy val submitElement = jQuery("#submitButton")
  lazy val searchElement = jQuery("#searcher")
  var searching = false
  lazy val searchField = dom.document.createElement("input")

  @js.native
  def main(): Unit = {
    jQuery(() => {

      println("Hey")
      jQuery("#user").append("<p>Signed in as " + usernameElement.value + "</p>")
      submitElement.click {
        (_: JQueryEvent) => {
          println(usernameElement.value)
          dom.window.localStorage.setItem("scalol_username", usernameElement.value.toString)
          val credentials = new SignupData(emailElement.value.toString, usernameElement.value.toString, passwordElement.value.toString)
          val gson = new Gson
          val jsonString = gson.toJson(credentials)
          println("==========")
          println(jsonString)
          println("==========")
          //          val thing = Http(Util.url).postData(credentials).header("content-type", "application/json").asString.code
//          println(thing)
        }
      }
    })
  }

}

class SignupData(email: String, username: String, password: String) {
  override def toString: String = {
    "{\"email\":\"" + email + "\",\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"
  }
}