package tutorial.webapp

import org.scalajs.dom
import org.scalajs.jquery.jQuery

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * Created by henrik on 19/05/17.
  */
@JSExportTopLevel("Login")
object Login {
  lazy val usernameElement = jQuery("#username")
  lazy val passwordElement = jQuery("#password")
  lazy val submitElement = jQuery("#loginButton")

  @JSExport
  def start(): Unit = {
    jQuery(() => {
      submitElement.click {
        (_: JQueryEvent) => {
          println("login")


        }
      }
    })
  }

  @JSExport
  def login(): Unit = {
    val credentials = new LoginData(usernameElement.value.toString, passwordElement.value.toString)

    dom.ext.Ajax.post(
      url = Util.authUrl,
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
  }
}

class LoginData(username: String, password: String) {
  override def toString: String = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"
}
