package tutorial.webapp


import org.scalajs._
import org.scalajs.dom.raw.{Blob, File, FileReader}
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import js.Dynamic._
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSGlobal, ScalaJSDefined}

object Main extends js.JSApp {
  lazy val emailElement = jQuery("#email")
  lazy val usernameElement = jQuery("#username")
  lazy val passwordElement = jQuery("#password")
  lazy val repeatPasswordElement = jQuery("#repeatPassword")
  lazy val submitElement = jQuery("#submitButton")
  lazy val searchElement = jQuery("#searcher")
  var searching = false
  lazy val searchField = dom.document.createElement("input")

  var posts: js.Array[Post] = new js.Array[Post]()

  @js.native
  def main(): Unit = {
    jQuery(() => {
      Util.loadNavbar
      new Signup().start()
      new Login().start()

      val xhr = new dom.XMLHttpRequest()
      xhr.open("GET",
        "src/main/resources/posts.json"
      )
      xhr.onload = { (e: dom.Event) =>
        if (xhr.status == 200) {
          val jsonposts: js.Array[js.Dynamic] = JSON.parse(xhr.response.toString).asInstanceOf[js.Array[js.Dynamic]]
          for (post <- jsonposts) {
            val j = new Post(post.id, 0, post.title, 0, post.tags, true, post.image)
            jQuery("#posts").append(j.toHtml)
          }
        }
      }
      xhr.send()
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

class Post(id: js.Dynamic, score: Int, title: js.Dynamic, owner_id: Integer, tags: js.Dynamic, nsfw: Boolean, image: js.Dynamic) {
  def toHtml: String = {
    var stringToBuild: String = "<div class=\"post\">"
    stringToBuild += "<h1>" + title + "</h1><br>"
    stringToBuild += "<img src=\"" + image + "\">"

    stringToBuild += "</div>"
    stringToBuild
  }
}