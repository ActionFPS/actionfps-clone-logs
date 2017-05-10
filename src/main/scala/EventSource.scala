import io.scalajs.nodejs.events.IEventEmitter

import scala.scalajs.js
import scala.scalajs.js.Dictionary
import scala.scalajs.js.annotation.JSImport

/**
  * Created by william on 10/5/17.
  *
  * Mapping to <a href="https://github.com/EventSource/eventsource/">EventSource library</a>
  */
@js.native
@JSImport("eventsource", JSImport.Namespace)
class EventSource() extends IEventEmitter {
  def this(url: String) = this()
  def this(url: String, eventSourceInitDict: Dictionary[_]) = this()
}
