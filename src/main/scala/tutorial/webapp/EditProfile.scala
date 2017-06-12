package tutorial.webapp

import org.scalajs._
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobal, ScalaJSDefined}
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

/**
  * Created by henrik on 6/8/17.
  */
@JSExportTopLevel("EditProfile")
object EditProfile {
  @JSExport
  def update(): Unit = {
    val newPassword = jQuery("#password").value.toString
    val password = jQuery("#repeatPassword").value.toString
    if (password.equals(newPassword)) {
      val newUsername = jQuery("#newUsername").value.toString
      println(newUsername)
      val newEmail = jQuery("#newEmail").value.toString
      val data = new SignupData(newEmail, newUsername, newPassword).toString

      Util.patch(Util.userUrl, data, Util.jsonAndTokenHeaderMap, (xhr: XMLHttpRequest) => {
        if (xhr.status == 200) {
          val x = JSON.parse(xhr.responseText)
          println(x)
          dom.window.localStorage.setItem(
            "scalol_username", newUsername
          )
          dom.window.location.href = "./index.html"
        }
      })
    } else {
      Util.showError("Passwords don't match", "try again")
    }
  }

  @JSExport
  def delete(): Unit = {
    dom.ext.Ajax.delete(
      url = Util.userUrl,
      headers = Map("auth" -> dom.window.localStorage.getItem("scalol_token"))
    ).foreach {
      xhr =>
        if (xhr.status == 200) {
          println(JSON.parse(xhr.responseText))
          Main.logout()
        } else {
          Util.showError(xhr.responseText, null)
        }
    }
  }
}
