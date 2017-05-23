package tutorial.webapp

import org.scalajs.jquery.jQuery

import scala.scalajs.js

/**
  * Created by henrik on 19/05/17.
  */
class Login {
  lazy val usernameElement = jQuery("#username")
  lazy val passwordElement = jQuery("#password")
  lazy val submitElement = jQuery("#loginButton")

  def start(): Unit = {
    jQuery(() => {
      submitElement.click {
        (_: JQueryEvent) => {
          println("login")
        }
      }
    })
  }
}

class LoginData(username: String, password: String) {
  override def toString: String = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"
}
