package tutorial.webapp


import org.scalajs._
import org.scalajs.dom.raw.{Blob, File, FileReader}
import org.scalajs.jquery.jQuery
import tutorial.webapp.Main.nextElement

import scala.scalajs.js
import js.Dynamic._
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobal, ScalaJSDefined}
import scala.scalajs.js.JSON

@JSExportTopLevel("Main")
object Main {
  lazy val nextElement = jQuery("#next")
  lazy val emailElement = jQuery("#email")
  lazy val usernameElement = jQuery("#username")
  lazy val passwordElement = jQuery("#password")
  lazy val repeatPasswordElement = jQuery("#repeatPassword")
  lazy val submitElement = jQuery("#submitButton")
  lazy val searchElement = jQuery("#searcher")
  var searching = false
  lazy val searchField = dom.document.createElement("input")
  var lowestId: Int = -1
  var posts: js.Array[Post] = new js.Array[Post]()

  def loggedIn(): Boolean = dom.window.localStorage.getItem("scalol_token") != null

  @js.native
  @JSExport
  def main(): Unit = {
    loadmore()
  }

  @JSExportTopLevel("logout")
  def logout(): Unit = {
    dom.window.localStorage.removeItem("scalol_token")
    dom.window.localStorage.removeItem("scalol_username")
    Util.loadNavbar()
    dom.window.location.href = "./index.html"
  }

  @JSExportTopLevel("upvote")
  def upvote(id: Int): Unit = {
    println("Upvote " + id)
    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET",
      Util.upvoteUrl + "/" + id
    )
    xhr.setRequestHeader("auth", dom.window.localStorage.getItem("scalol_token"))
    xhr.onload = { (e: dom.Event) =>
      if (xhr.status == 200) {
        println(xhr.response.toString)
      }
    }
    xhr.send()
  }

  @JSExportTopLevel("downvote")
  def downvote(id: Int): Unit = {
    println("Downvote: " + id)
    var xhr = new dom.XMLHttpRequest()
    println(xhr)
    xhr.open("GET",
      Util.downvoteUrl + "/" + id
    )
    xhr.setRequestHeader("auth", dom.window.localStorage.getItem("scalol_token"))
    xhr.onload = { (e: dom.Event) =>
      if (xhr.status == 200) {
        println(xhr.response.toString)
      }
    }
    xhr.send()
  }

  @JSExportTopLevel("loadmore")
  def loadmore(): Unit = {
    //    http://nixme.ddns.net/posts?offset=6&number=2
    println("HELLELELE")
    var xhr = new dom.XMLHttpRequest()
    var url = ""
    if (this.lowestId == -1) {
      url = Util.postUrl
    } else {
      url = Util.postUrl + "?offset=" + (this.lowestId - 1) + "&number=2"
    }
    xhr.open("GET",
      url
    )
    xhr.onload = { (e: dom.Event) =>
      if (xhr.status == 200) {
        val jsPosts: js.Array[js.Dynamic] = JSON.parse(xhr.response.toString).asInstanceOf[js.Array[js.Dynamic]]
        println("Lowest id is = " + this.lowestId)
        if (this.lowestId == -1) this.lowestId = jsPosts.apply(0).id.asInstanceOf[Int]
        println("Now lowest id is " + this.lowestId)
        for (jsPost <- jsPosts) {
          if (jsPost.id.asInstanceOf[Int] < this.lowestId) {
            this.lowestId = jsPost.id.asInstanceOf[Int]
            println("Now lowest id is " + this.lowestId)
          }

          val postToAdd = parse(jsPost, "post")
          if (!(!loggedIn() && jsPost.nsfw.asInstanceOf[Boolean]))
            jQuery("#posts").append(postToAdd.toHtml)
        }
      }
    }
    xhr.send()

  }

  def parse(obj: js.Dynamic, typeOfObject: String): HtmlObject = {
    typeOfObject match {
      case "post" => new Post(obj.id, obj.score.asInstanceOf[Int], obj.title, obj.owner_id.asInstanceOf[Int], obj.nsfw.asInstanceOf[Boolean], obj.image_path)
      case "user" => new User()
      case "comment" => new Comment(obj.username.asInstanceOf[String], obj.content.asInstanceOf[String])
      case "message" => new Message()
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


  override def toHtml: String = {

    var stringToBuild: String = "<div class=\"post\">"
    stringToBuild += "<div class=\"postContent\">"
    stringToBuild += "<a href=\"./posts.html?" + id + "\"><h1 class=\"postTitle\">" + title + "</h1></a>"
    stringToBuild += "<h2>Score: " + score + "</h2>"
    stringToBuild += "<img src=\"" + image_path + "\" style=\"margin-left: 50%; transform: translate(-50%, 0%)\"><br>"
    if (dom.window.localStorage.getItem("scalol_token") != null) {
      stringToBuild += "<div style=\"position: relative; margin-left: 50%; transform: translate(-50%, 0%)\" >"
      stringToBuild += "<button id=\"upvote" + id + "\" onclick=upvote(" + id + ") type=\"button\" class=\"btn btn-default btn-lg\"><span class=\"glyphicon glyphicon-thumbs-up\" aria-hidden=\"true\"></span>Upvote</button>"
      stringToBuild += "<button id=\"downvote" + id + "\" onclick=downvote(" + id + ") type=\"button\" class=\"btn btn-default btn-lg\"><span class=\"glyphicon glyphicon-thumbs-down\" aria-hidden=\"true\"></span>Downvote</button>"
      stringToBuild += "</div>"
    }
    stringToBuild += "</div></div>"
    stringToBuild
  }
}

class User extends HtmlObject {
  override def toHtml: String = ???
}

class Comment(username: String, content: String) extends HtmlObject {
  override def toHtml: String = "<div class=\"othercomment\"><h2>" + username + ": </h2><p>" + content + "</p></div>"
}

class Message extends HtmlObject {
  override def toHtml: String = ???
}