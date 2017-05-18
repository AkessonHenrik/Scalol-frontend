package tutorial.webapp


import org.scalajs._
import org.scalajs.jquery.jQuery

import scala.scalajs.js

import scala.scalajs.js.annotation.{JSExport, JSGlobal, ScalaJSDefined}

object TutorialApp extends js.JSApp {
  lazy val emailElement = jQuery("#email")
  lazy val phoneElement = jQuery("#phone")
  lazy val passwordElement = jQuery("#password")
  lazy val repeatPasswordElement = jQuery("#repeatPassword")
  lazy val submitElement = jQuery("#submitButton")
  lazy val searchElement = jQuery("#searcher")
  var searching = false
  lazy val searchField = dom.document.createElement("input")

  @js.native
  def main(): Unit = {
    jQuery(() => {
      jQuery("#user").append("<p>Signed in as " + emailElement.value + "</p>")
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
          println(emailElement.value)
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