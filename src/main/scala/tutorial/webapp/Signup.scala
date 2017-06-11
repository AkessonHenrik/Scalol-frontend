package tutorial.webapp

import org.scalajs._
import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.util.{Failure, Success}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * Created by henrik on 19/05/17.
  */
@JSExportTopLevel("Signup")
object Signup {
  lazy val emailElement = jQuery("#email")
  lazy val usernameElement = jQuery("#username")
  lazy val passwordElement = jQuery("#password")
  lazy val repeatPasswordElement = jQuery("#repeatPassword")
  lazy val submitElement = jQuery("#signupButton")
  lazy val searchElement = jQuery("#searcher")
  var searching = false
  lazy val searchField = dom.document.createElement("input")

  def checkValidity(): Boolean = {
    passwordElement.value.equals(repeatPasswordElement.value)
  }

  var alert: Boolean = false;

  @JSExport
  def signup() = {
    jQuery(() => {

      jQuery("#user").append("<p>Signed in as " + usernameElement.value + "</p>")
      submitElement.click {
        (_: JQueryEvent) => {

          if (checkValidity()) {
            if (alert) {
              alert = false
              jQuery("#error").removeClass("alert alert-danger")
            }
            println(usernameElement.value)
            dom.window.localStorage.setItem("scalol_username", usernameElement.value.toString)
            val credentials = new SignupData(emailElement.value.toString, usernameElement.value.toString, passwordElement.value.toString)
            println("==========")
            println(credentials)
            println("==========")

            dom.ext.Ajax.post(
              url = Util.userUrl,
              data = credentials.toString,
              headers = Map("Content-Type" -> "application/json")
            ).foreach { xhr =>
              if (xhr.status == 200) {
                val x = JSON.parse(xhr.responseText)
                println(x.token)
                dom.window.localStorage.setItem(
                  "scalol_token", x.token.toString
                )
                dom.window.localStorage.setItem(
                  "scalol_username", usernameElement.value.toString
                )
                dom.window.location.href = "./index.html"
              }
            }
          } else if (!alert) {
            alert = true
            jQuery("#error").append("<div class=\"alert alert-danger\" role=\"alert\">" + "Passwords don't match, try again" + "</div>")
          }
        }
      }
    })
  }

}

class SignupData(email: String, username: String, password: String) {
  override def toString: String = "{\"mail\":\"" + email + "\",\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"
}