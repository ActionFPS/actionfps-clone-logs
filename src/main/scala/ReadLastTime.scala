import io.scalajs.nodejs
import io.scalajs.nodejs.readline.ReadlineOptions

import scala.concurrent.{Future, Promise}

object ReadLastTime {
  /**
    * Read the log to determine the last timestamp and number of times it appears
    */
  def getLastTimes(file: String): Future[Option[(String, Int)]] = {
    val readInterface = nodejs.readline.Readline.createInterface(
      new ReadlineOptions(
        input = nodejs.fs.Fs.createReadStream(file)
      ))
    val promise = Promise[Option[(String, Int)]]
    var haveTime: String = null
    var haveCount = 0
    readInterface.onLine { line =>
      haveCount = haveCount + 1
      val time = line.split("\t").head
      if (haveTime != time) {
        haveCount = 1
      }
      haveTime = time
    }
    readInterface.onClose(() => {
      promise.success {
        Option(haveTime).map { r =>
          (r, haveCount)
        }
      }
    })
    promise.future
  }
}
