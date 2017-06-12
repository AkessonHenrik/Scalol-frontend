package tutorial.webapp

import org.scalajs.dom
import org.scalajs.jquery.jQuery
import tutorial.webapp.Login.usernameElement
import tutorial.webapp.Main.parse

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.concurrent.ExecutionContext.Implicits.global._
import org.scalajs._
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.XMLHttpRequest
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.util.{Failure, Success}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js.JSON

/**
  * Created by henrik on 6/6/17.
  */
@JSExportTopLevel("PostView")
object PostView {
  val postId = js.URIUtils.decodeURIComponent(dom.window.location.toString.substring(dom.window.location.toString.indexOf('?') + 1)).toString

  @JSExport
  def start(): Unit = {

    this.getPost(postId)
    this.getComments(postId)
    if (dom.window.localStorage.getItem("scalol_token") != null) {
      jQuery("#comment").append(
        "<textarea rows=\"4\" cols=\"50\" id=\"commentInput\" placeholder=\"Enter comment\"></textarea>"
          + "<button id=\"submitComment\" onclick=PostView.postComment() type=\"button\" class=\"btn btn-default btn-lg\">Submit</button>"
      )
    } else {
      jQuery("#comment").append(
        "<div class=\"alert alert-danger\">\n  <strong>You cannot comment!</strong> Login or Sign up to comment.\n</div>"
      )
    }
  }

  def getPost(postId: String): Unit = {
    // Get post
    val url = Util.postUrl + "/" + postId
    Util.get(url, null, null, (xhr: dom.XMLHttpRequest) => {
      if (xhr.status == 200) {
        val jsPost: js.Dynamic = JSON.parse(xhr.response.toString)
        println("Post: " + JSON.stringify(jsPost))
        val postToAdd = parse(jsPost, "post")
        jQuery("#post").append(postToAdd.toHtml)
      } else if (xhr.status == 404) {
        Util.showError("Post not found", null)
      } else {
        Util.showError("An unexpected error occurred", "try again later")
      }
    })
  }

  def getComments(postId: String): Unit = {
    // Get comments
    val url = Util.commentUrl + "/" + postId
    Util.get(url, null, null, (xhr: XMLHttpRequest) => {
      if (xhr.status == 200) {
        println(xhr.responseText)
        val jsComments: js.Array[js.Dynamic] = JSON.parse(xhr.response.toString).asInstanceOf[js.Array[js.Dynamic]]
        println(JSON.stringify(jsComments))
        for (jsComment <- jsComments) {
          jQuery("#comments").append(parse(jsComment, "comment").toHtml)
        }
      } else if (xhr.status == 404) {
        Util.showError("Post not found", null)
      } else {
        Util.showError("An unexpected error occurred", "try again later")
      }

    }

    )
  }

  @JSExport
  def postComment(): Unit = {
    val commentData = jQuery("#commentInput").value
    val dataString = new CommentData(postId, commentData.toString).toString;
    println("Data string = " + dataString)
    Util.post(Util.commentUrl, dataString, Util.jsonAndTokenHeaderMap, (xhr: dom.XMLHttpRequest) => {
      if (xhr.status == 200) {
        val x = JSON.parse(xhr.responseText)
        println(x)
        dom.window.location.href = "./posts.html?" + postId
      }
    })
  }
}

class CommentData(postId: String, content: String) {
  override def toString: String = "{\"content\": \"" + content + "\", \"post_id\": " + postId + "}"
}