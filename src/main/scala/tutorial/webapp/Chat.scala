package tutorial.webapp

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.jquery.jQuery
import tutorial.webapp.Main.parse

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * Created by henrik on 6/9/17.
  */
@JSExportTopLevel("Chat")
object Chat {
  @JSExport
  def main(in: html.Input, pre: html.Pre) = {
  }

  @JSExport
  def startTalking(): Unit = {
//    val in = jQuery("#inputBox").asInstanceOf[html.Input]
//    val pre = jQuery("#outputBox").asInstanceOf[html.Pre]
//    println(jQuery("#dest").value)
//    val url = "ws://nixme.ddns.net/connect?token=" + dom.window.localStorage.getItem("scalol_token") + "&to=" + jQuery("#dest").value
//
//    val socket = new dom.WebSocket(url)
//    socket.onmessage = {
//      (e: dom.MessageEvent) =>
//        pre.textContent +=
//          e.data.toString
//    }
//    socket.onopen = { (e: dom.Event) =>
//      in.onkeyup = { (e: dom.Event) =>
//        socket.send(in.value)
//      }
//    }
  }
}
