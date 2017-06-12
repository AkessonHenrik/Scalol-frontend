package tutorial.webapp


import org.scalajs._
import org.scalajs.dom.raw.{Blob, CloseEvent, File, FileReader}
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobal, ScalaJSDefined}
import scala.scalajs.js.JSON

// Main object, handles notifications and front page content
@JSExportTopLevel("Main")
object Main extends js.JSApp {
  // Stores current lowest post id
  var lowestId: Int = -1

  def loggedIn(): Boolean = dom.window.localStorage.getItem("scalol_token") != null

  def main(): Unit = {
    if (loggedIn()) {
      val url = "wss://nixme.ddns.net/notification?token=" + dom.window.localStorage.getItem("scalol_token")
      val socket = new dom.WebSocket(url)
      socket.onmessage = {
        (e: dom.MessageEvent) => {
          val content = e.data.toString
          val username = content.substring(0, content.indexOf(" wants to chat with you"))
          if (username.length > 0)
            jQuery("#notifications").append("<p><a class=\"redirect\" href=\"./chat.html?" + username + "\">" + content + "</a></p>")
          else
            jQuery("#notifications").append("<p>" + content + "</p>")
          jQuery("#notifications").scrollTop(jQuery("#notifications").apply(0).scrollHeight)
        }
      }
      socket.onclose = { (e: CloseEvent) => {
        jQuery("#notifications").append("<span class=\"socketClosed\">The connection has been closed </span><br>")
      }
      }
    }
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
    val url = Util.upvoteUrl + "/" + id
    Util.get(url, null, Util.jsonAndTokenHeaderMap, (xhr: dom.XMLHttpRequest) => {
      if (xhr.status == 200) {
        println(xhr.response.toString)

      }
    })
  }

  @JSExportTopLevel("downvote")
  def downvote(id: Int): Unit = {
    val url = Util.downvoteUrl + "/" + id
    Util.get(url, null, Util.jsonAndTokenHeaderMap, (xhr: dom.XMLHttpRequest) => {
      if (xhr.status == 200) {
        println(xhr.response.toString)
      }
    })
  }

  @JSExport
  def loadmore(): Unit = {
    var url = ""
    /*
    * The server's posts endpoint can be reached in two different manners
    * - Either no url parameters are specified, in which case the last 100 posts are returned
    * - Either an offset (last fetched post) and a number of posts to get is specified
    * Here, the lowest id will be -1 by default if the page has just been loaded
    * If posts have been loaded before (which means the "More" button has been pressed),
    * a number of posts to fetch (10 by default) and an offset (the current lowest post id) is sent
    */
    if (lowestId == -1) {
      url = Util.postUrl
    } else {
      url = Util.postUrl + "?offset=" + (this.lowestId - 1) + "&number=10"
    }

    Util.get(url, null, null, (xhr: dom.XMLHttpRequest) => {
      if (xhr.status == 200) {
        // Parsing the result as an array of js.Dynamic
        val jsPosts: js.Array[js.Dynamic] = JSON.parse(xhr.response.toString).asInstanceOf[js.Array[js.Dynamic]]
        // If the page has just been loaded, the lowestId is updated as the first post Id
        // It will then be updated as soon as a post with a lower id is interpreted
        if (this.lowestId == -1) this.lowestId = jsPosts.apply(0).id.asInstanceOf[Int]
        for (jsPost <- jsPosts) {
          if (jsPost.id.asInstanceOf[Int] < this.lowestId) {
            this.lowestId = jsPost.id.asInstanceOf[Int]
          }

          // Create a new Post instance, add it's html representation to the page content
          val postToAdd = parse(jsPost, "post")
          if (!(!loggedIn() && jsPost.nsfw.asInstanceOf[Boolean]))
            jQuery("#posts").append(postToAdd.toHtml)
        }
      }
    })
  }

  def parse(obj: js.Dynamic, typeOfObject: String): HtmlObject = {
    typeOfObject match {
      case "post" => new Post(obj.id, obj.score.asInstanceOf[Int], obj.title, obj.owner, obj.nsfw.asInstanceOf[Boolean], obj.image_path)
      case "comment" => new Comment(obj.username.asInstanceOf[String], obj.content.asInstanceOf[String])
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

class Post(argId: js.Dynamic, argScore: Int, argTitle: js.Dynamic, argOwner: js.Dynamic, argNsfw: Boolean, argImage: js.Dynamic) extends HtmlObject {
  def id = argId

  def score = argScore

  def title = argTitle

  def nsfw = argNsfw

  def image_path = argImage

  def owner = argOwner

  override def toHtml: String = {

    var stringToBuild: String = "<div id=\"post" + id + "\" class=\"post\">"
    stringToBuild += "<div class=\"postContent\">"
    stringToBuild += "<a href=\"./posts.html?" + id + "\"><h1 class=\"postTitle\">" + title + "</h1></a>"
    if (owner.isInstanceOf[String])
      stringToBuild += "<h2>Posted by: <a href=\"./user.html?" + owner + "\">" + owner + "</a></h2>"
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

class User(username: String, mail: String) extends HtmlObject {
  override def toHtml: String = "<h1>" + username + "'s posts:</h1>"
}

class Comment(username: String, content: String) extends HtmlObject {
  override def toHtml: String = "<div class=\"othercomment\"><h2>" + username + ": </h2><p>" + content + "</p></div>"
}