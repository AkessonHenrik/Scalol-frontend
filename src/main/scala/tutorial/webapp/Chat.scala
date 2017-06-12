package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.raw.CloseEvent
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSON
import scala.scalajs.js

/**
  * Created by henrik on 6/9/17.
  */
@JSExportTopLevel("Chat")
object Chat {

  @JSExport
  def startTalking(): Unit = {
    val recipient = jQuery("#dest").value

    val ajaxUrl = Util.messageUrl + "/" + recipient
    Util.get(ajaxUrl, null, Util.jsonAndTokenHeaderMap, (xhr: dom.XMLHttpRequest) => {
      if (xhr.status == 200) {
        val messages: js.Array[js.Dynamic] = JSON.parse(xhr.responseText).asInstanceOf[js.Array[js.Dynamic]]
        for (message <- messages) {
          println(JSON.stringify(message))
          if (message.from == recipient) {
            jQuery("#chatContent").append("<span class=\"otherMessage\">" + recipient + ":  </span><span>" + message.content + "</span><br>")
          } else {
            jQuery("#chatContent").append("<span class=\"otherMessage\">" + "You:  </span><span>" + message.content + "</span><br>")
          }
        }
        jQuery("#chatContent").scrollTop(jQuery("#chatContent").apply(0).scrollHeight)
      }
    })

    val in = jQuery("#inputBox")
    val url = "wss://nixme.ddns.net/connect?token=" + dom.window.localStorage.getItem("scalol_token") + "&to=" + recipient
    val socket = new dom.WebSocket(url)
    socket.onmessage = {
      (e: dom.MessageEvent) => {
        jQuery("#chatContent").append("<span class=\"otherMessage\">" + recipient + ":  </span><span>" + e.data.toString.substring(e.data.toString.indexOf("]") + 1) + "</span><br>")
        jQuery("#chatContent").scrollTop(jQuery("#chatContent").apply(0).scrollHeight)
      }
    }
    socket.onclose = { (e: CloseEvent) => {
      jQuery("#chatContent").append("<span class=\"socketClosed\">This user has blocked you. The connection has been closed </span><br>")
      jQuery("#userInput").remove()
    }
    }
    jQuery("#sendMessage").click(() => {
      val newMessage: String = in.value.asInstanceOf[String]

      socket.send(newMessage)
      in.value("")
      jQuery("#chatContent").append("<span class=\"userMessage\">" + "You: </span><span>" + newMessage + "</span><br>")
      jQuery("#chatContent").scrollTop(jQuery("#chatContent").apply(0).scrollHeight)
    })
    jQuery("#blockButton").click(() => {
      val ajaxUrl = Util.blockUrl + recipient
      Util.get(ajaxUrl, null, Util.jsonAndTokenHeaderMap, (xhr: dom.XMLHttpRequest) => {
        if (xhr.status == 200) {
          println(xhr.responseText)
        }
      })
    })
    jQuery("#unblockButton").click(() => {
      val ajaxUrl = Util.unblockUrl + recipient
      Util.get(ajaxUrl, null, Util.jsonAndTokenHeaderMap, (xhr: dom.XMLHttpRequest) => {
        if (xhr.status == 200) {
          println(xhr.responseText)
        }
      })
    })
  }

}
