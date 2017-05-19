package tutorial.webapp


import org.scalajs._
import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, ScalaJSDefined}
import scalaj.http.Http

object Main extends js.JSApp {
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
      Util.loadNavbar

      jQuery("#user").append("<p>Signed in as " + usernameElement.value + "</p>")
      searchElement.click {
        (_: JQueryEvent) => {
          if (!searching) {
            searching = true
            searchElement.append(searchField)
          } else {
            //            searchElement.remove(searchField)
          }
        }
      }
      submitElement.click {
        (_: JQueryEvent) => {
          println(usernameElement.value)
          dom.window.localStorage.setItem("scalol_username", usernameElement.value.toString)
          val credentials = new SignupData(emailElement.value.toString, usernameElement.value.toString, passwordElement.value.toString)
          println("==========")
          println(credentials)
          println("==========")
          Ajax.post(Util.url, credentials.toString).foreach(e => {
            case xhr =>
              println(xhr)
          })
        }
      }
    })
  }
}

@ScalaJSDefined
trait JQuery extends js.Object {
  def click(handler: JQueryEvent => Any): Unit
}

@js.native
@JSGlobal("jQuery.Event")
class JQueryEvent(name: String) extends js.Object {
}