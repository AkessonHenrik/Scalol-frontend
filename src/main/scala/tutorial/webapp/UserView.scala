package tutorial.webapp

import org.scalajs.dom
import org.scalajs.jquery.jQuery
import tutorial.webapp.Main.{loggedIn, parse}

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * Created by henrik on 6/6/17.
  */
@JSExportTopLevel("UserView")
object UserView {

  val username = js.URIUtils.decodeURIComponent(dom.window.location.toString.substring(dom.window.location.toString.indexOf('?') + 1)).toString

  @JSExport
  def start(): Unit = {
    println("User: " + username)
    getUser(username)
  }

  def getUser(username: String): Unit = {
    // Get user & posts
    val xhr = new dom.XMLHttpRequest()
    val url = Util.userUrl + "/" + username
    xhr.open("GET",
      url
    )
    xhr.onload = { (e: dom.Event) =>
      if (xhr.status == 200) {
        println(JSON.stringify(xhr.response))
        val response = JSON.parse(xhr.response.toString)
        jQuery("#info").append(new User(response.user.username.asInstanceOf[String], response.user.mail.asInstanceOf[String]).toHtml)
        val jsPosts: js.Array[js.Dynamic] = response.posts.asInstanceOf[js.Array[js.Dynamic]]
        for (jsPost <- jsPosts) {
          val postToAdd = parse(jsPost, "post")
          if (!(!loggedIn() && jsPost.nsfw.asInstanceOf[Boolean]))
            jQuery("#posts").append(postToAdd.toHtml)
        }
      } else if(xhr.status == 404) {
        jQuery("#info").append(
          "<div class=\"alert alert-danger\">\n  <strong>User not found</strong></div>"
        )
      }
    }
    xhr.send()
  }
}
