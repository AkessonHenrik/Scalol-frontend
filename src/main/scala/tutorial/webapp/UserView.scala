package tutorial.webapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * Created by henrik on 6/6/17.
  */
@JSExportTopLevel("UserView")
class UserView {
  @JSExport
  def start(): Unit = {
    println("Hey")
  }
}
