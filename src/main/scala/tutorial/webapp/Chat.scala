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
  def main(in: html.Input,
           pre: html.Pre) = {
//    val echo = "ws://echo.websocket.org"
//
//    val socket = new dom.WebSocket(echo)
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
val xhr = new dom.XMLHttpRequest()
    val url = Util.url + "/connect?token=" + dom.window.localStorage.getItem("scalol_token") + "&to=yee"
    xhr.open("GET",
      url
    )
    xhr.onload = { (e: dom.Event) =>
      if (xhr.status == 200) {
        println(JSON.parse(xhr.response.toString))
      }
    }

    xhr.send()
  }
}
