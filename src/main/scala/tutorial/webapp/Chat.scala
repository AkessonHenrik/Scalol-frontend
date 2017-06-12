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

  /* This function is called as soon as the page is loaded
  * If no url parameters are specified (username to start chatting with), a user action will be needed
  * meaning entering a username and pressing the button
  * Otherwise, the actions are programmatically triggered and a conversation will be started with the specified user
  * */
  @JSExport
  def predefinedChat(): Unit = {
    val startIndex: Int = (dom.window.location.toString.indexOf('?') + 1)
    if (startIndex > 0) {
      val predefinedUser: String = js.URIUtils.decodeURIComponent(dom.window.location.toString.substring(startIndex)).toString
      if (predefinedUser.length > 0) {
        jQuery("#dest").value(predefinedUser)
        startTalking()
      }
    }
  }

  @JSExport
  def startTalking(): Unit = {
    val recipient = jQuery("#dest").value

    // Get conversation history
    val ajaxUrl = Util.messageUrl + "/" + recipient
    Util.get(ajaxUrl, null, Util.jsonAndTokenHeaderMap, (xhr: dom.XMLHttpRequest) => {
      if (xhr.status == 200) {
        val messages: js.Array[js.Dynamic] = JSON.parse(xhr.responseText).asInstanceOf[js.Array[js.Dynamic]]
        for (message <- messages) {
          println(JSON.stringify(message))
          if (message.from == recipient) {
            jQuery("#chatContent").append("<span class=\"otherMessage\">" + recipient + ":  " + message.content + "</span><br>")
          } else {
            jQuery("#chatContent").append("<span class=\"myMessage\">" + "You: " + message.content + "</span><br>")
          }
        }
        jQuery("#chatContent").scrollTop(jQuery("#chatContent").apply(0).scrollHeight)
      }
    })

    // Socket actions
    val in = jQuery("#inputBox")
    val url = "wss://nixme.ddns.net/connect?token=" + dom.window.localStorage.getItem("scalol_token") + "&to=" + recipient
    val socket = new dom.WebSocket(url)
    socket.onmessage = {
      (e: dom.MessageEvent) => {
        jQuery("#chatContent").append("<span class=\"otherMessage\">" + recipient + ": " + e.data.toString.substring(e.data.toString.indexOf("]") + 1) + "</span><br>")
        jQuery("#chatContent").scrollTop(jQuery("#chatContent").apply(0).scrollHeight)
      }
    }
    socket.onclose = { (e: CloseEvent) => {
      jQuery("#chatContent").append("<span class=\"socketClosed\">This user has blocked you. The connection has been closed </span><br>")
      jQuery("#userInput :input").prop("disabled", true)
    }
    }
    jQuery("#sendMessage").click(() => {
      val newMessage: String = in.value.asInstanceOf[String]

      // Send message
      socket.send(newMessage)
      // Clear input content
      in.value("")

      // Add message to conversation window
      jQuery("#chatContent").append("<span class=\"userMessage\">" + "You: </span><span>" + newMessage + "</span><br>")
      // Scroll to bottom of conversation automatically
      jQuery("#chatContent").scrollTop(jQuery("#chatContent").apply(0).scrollHeight)
    })

    // Blocking a user
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
          startTalking()
        }
      })
    })
  }

}
