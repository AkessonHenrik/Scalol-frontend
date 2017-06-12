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
  def login(): Unit = {
    val credentials = new LoginData(usernameElement.value.toString, passwordElement.value.toString)
    val url = Util.authUrl
    val data = credentials.toString
    val headers = Map("Content-Type" -> "application/json")
    Util.post(url, data, headers, (xhr: dom.XMLHttpRequest) => {
      if (xhr.status == 200) {
        val response = JSON.parse(xhr.responseText)
        println(response.token)
        dom.window.localStorage.setItem(
          "scalol_token", response.token.toString
        )
        dom.window.localStorage.setItem(
          "scalol_username", usernameElement.value.toString
        )
        dom.window.location.href = "./index.html"
      }
    })
  }
}

class LoginData(username: String, password: String) {
  override def toString: String = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"
}
