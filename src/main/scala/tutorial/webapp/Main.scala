package tutorial.webapp


import org.scalajs._
import org.scalajs.dom.raw.{Blob, File, FileReader}
import org.scalajs.jquery.jQuery
import tutorial.webapp.Main.nextElement

import scala.scalajs.js
import js.Dynamic._
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSGlobal, ScalaJSDefined}

object Main extends js.JSApp {
  lazy val nextElement = jQuery("#next")
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
      new newPost().start()

      nextElement.click {
        (_: JQueryEvent) => {
          println("Next page")
        }
      }
      val xhr = new dom.XMLHttpRequest()
      xhr.open("GET",
        Util.postUrl
      )
      xhr.onload = { (e: dom.Event) =>
        if (xhr.status == 200) {
          val jsonposts: js.Array[js.Dynamic] = JSON.parse(xhr.response.toString).asInstanceOf[js.Array[js.Dynamic]]
          for (post <- jsonposts) {
            val j = parse(post, "post")
            jQuery("#posts").append(j.toHtml)
          }
        }
      }
      xhr.send()
    })
  }


  def parse(obj: js.Dynamic, typeOfObject: String): HtmlObject = {
    typeOfObject match {
      case "post" => {
        new Post(obj.id, obj.score.asInstanceOf[Int], obj.title, obj.owner_id.asInstanceOf[Int], obj.nsfw.asInstanceOf[Boolean], obj.image_path)
      }
      case "user" => {
        new User()
      }
      case "comment" => {
        new Comment()
      }
      case "message" => {
        new Message()
      }

    }
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

trait HtmlObject {
  def toHtml: String
}

class Post(argId: js.Dynamic, argScore: Int, argTitle: js.Dynamic, argOwner_id: Integer, argNsfw: Boolean, argImage: js.Dynamic) extends HtmlObject {
  def id = argId

  def score = argScore

  def title = argTitle

  def nsfw = argNsfw

  def image_path = argImage

  lazy val upvote = jQuery("#updvote" + id)

  lazy val downvote = jQuery("#downdvote" + id)

  def activate(): Unit = {
    downvote.click {
      (_: JQueryEvent) => {
        println("Downvote " + id)
      }
    }
  }

  this.activate()

  override def toHtml: String = {

    var stringToBuild: String = "<div class=\"post\">"
    stringToBuild += "<h1 class=\"postTitle\">" + title + "</h1>"
    stringToBuild += "<h2>Score: " + score + "</h2>"
    stringToBuild += "<img src=\"" + image_path + "\"><br>"
    stringToBuild += "<button id=\"upvote" + id + "\" style=\"margin-left: 100px\" type=\"button\" class=\"btn btn-default btn-lg\"><span class=\"glyphicon glyphicon-thumbs-up\" aria-hidden=\"true\"></span>Upvote</button>"
    stringToBuild += "<button id=\"downvote" + id + "\" type=\"button\" class=\"btn btn-default btn-lg\"><span class=\"glyphicon glyphicon-thumbs-down\" aria-hidden=\"true\"></span>Downvote</button>"
    stringToBuild += "</div>"

    stringToBuild
  }
}

class User extends HtmlObject {
  override def toHtml: String = ???
}

class Comment extends HtmlObject {
  override def toHtml: String = ???
}

class Message extends HtmlObject {
  override def toHtml: String = ???
}