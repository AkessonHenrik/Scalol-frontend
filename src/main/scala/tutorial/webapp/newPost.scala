package tutorial.webapp

import org.scalajs.dom._
import org.scalajs.jquery.jQuery
import org.scalajs._
import org.scalajs.dom.ext.Ajax
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.util.{Failure, Success}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js.JSON

/**
  * Created by henrik on 6/4/17.
  */
class newPost(/*postTitle: js.Dynamic, filePath: js.Dynamic, url: js.Dynamic, postTags: js.Dynamic*/) {
  //  val title = postTitle
  //  val file = filePath
  //  val imgUrl = url
  //  val tags = postTags

  def start() = {
    lazy val submitElement = jQuery("#postButton")
    jQuery(() => {
      submitElement.click {
        (_: JQueryEvent) => {
          //          val form = jQuery("#form").prop("files")(0) // You need to use standard javascript object here
          val form = jQuery("#file").get(0) // You need to use standard javascript object here

          val fuck = new FormData
          println(form)
          fuck.append("picture", form)
          println(fuck)
          val token = dom.window.localStorage.getItem("scalol_token")
          //          dom.ext.Ajax.post(
          //            url = "http://nixme.ddns.net:9000/upload",
          ////                        url = "http://localhost:3000",
          //            data = fuck,
          //            headers = Map("auth" -> token)
          //          ).foreach { xhr =>
          //            if (xhr.status == 200) {
          //              val x = JSON.parse(xhr.responseText)
          //              println(x)
          //            }
          //          }
        }
      }
    })
  }

  def create() = {
    jQuery(() => {
      //      val file: dom.File = fileReader.readAsArrayBuffer(new Blob(filePath.asInstanceOf[String]))
      //      jQuery("#file").value()

    })
  }
}
