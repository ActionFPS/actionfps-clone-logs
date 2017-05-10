import scala.scalajs.js
import js._
import io.scalajs.nodejs._

object AfCloneLogsApp extends JSApp {
  import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  private val DefaultStartTimeEnv = "DEFAULT_START_TIME"

  private val AuthorizationEnv = "AUTHORIZATION"

  private val defaultStartTime: String =
    process.env.get(DefaultStartTimeEnv).getOrElse("2016-01-02T03:04:05Z")

  private val authorization: Option[String] =
    process.env.get(AuthorizationEnv)

  def main(): Unit = {

    val atFile = {
      val targetFile = process.argv.toList.drop(2).lastOption.getOrElse {
        console.error("Target file not specified.")
        process.exit(1)
        null
      }
      console.info(s"Reading file ${targetFile}...")
      AtEventFile(targetFile)
    }

    for {
      optionalLastTime <- atFile.getLastLogTime
    } {
      val fromTime = optionalLastTime.map(_.timeString).getOrElse(defaultStartTime)
      var remainingIgnore = optionalLastTime.map(_.recordsAtTime).getOrElse(0)

      console.info(s"Resuming from time ${fromTime}, with ${remainingIgnore} lines at this time")
      val headers = Dictionary("Last-Event-Id" -> fromTime)
      authorization.foreach { authorization =>
        headers += "Authorization" -> authorization
      }
      val eventSourceInitDict =
        Dictionary("headers" -> headers)
      val es = new EventSource("https://actionfps.com/logs", eventSourceInitDict)
      es.on("open", { _: js.Dynamic =>
        console.log("EventSource connection opened.")
      })
      es.on("error", { e: js.Dynamic =>
        console.log("EventSource error: ", JSON.stringify(e))
      })
      es.on(
        "log", { e: js.Dynamic =>
          val line = e.data.toString
          if (remainingIgnore == 0) {
            atFile.appendLine(line)
          } else {
            remainingIgnore = remainingIgnore - 1
          }
        }
      )
    }
  }
}
