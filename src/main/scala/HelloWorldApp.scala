import scala.scalajs.js
import js._
import io.scalajs.nodejs._

import scala.concurrent.ExecutionContextExecutor

object HelloWorldApp extends JSApp {
  import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def main(): Unit = {
//    process.argv.lastOption.orElse(Some("fng")) match {
    Some("fng") match {
      case Some(filename) =>
        val timesGet = ReadLastTime.getLastTimes(filename)
        timesGet.foreach { (lastTimes: Option[(String, Int)]) =>
          val fromTime = lastTimes.map(_._1).getOrElse("2016-01-02T03:04:05Z")

//          val request = Dynamic.global.require("request")
//          val rq = request(s"https://actionfps.com/logs.tsv?from=${fromTime}")
//          val req = rq
//            .pipe(fs.Fs.createWriteStream(filename, Dictionary("flags" -> "a")))
//
//          import scala.scalajs.js.DynamicImplicits._
//          rq.on(
//            "end", { e: js.Dynamic =>
//              ReadLastTime.getLastTimes(filename).foreach {
//                rxz =>
          val eventSource = Dynamic.global.require("eventsource")
          val eventSourceInitDict =
            Dictionary("headers" -> Dictionary("Last-Event-Id" -> fromTime))
          var remainingIgnore = lastTimes.map(_._2).getOrElse(0)
          val es =
            Dynamic.newInstance(eventSource)("https://actionfps.com/logs", eventSourceInitDict)
          es.addEventListener("error", { e: js.Dynamic =>
            console.log("e", JSON.stringify(e))
          })
          es.addEventListener(
            "log", { e: js.Dynamic =>
              val line = e.data.toString
              if (remainingIgnore == 0) {
                fs.Fs.appendFileSync(filename, line + "\n")
              } else {
                remainingIgnore = remainingIgnore - 1
              }
            }
          )
//              }
//            }
//          )
        }
    }
  }
}
