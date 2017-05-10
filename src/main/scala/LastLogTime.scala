import io.scalajs.nodejs
import io.scalajs.nodejs.readline.ReadlineOptions

import scala.concurrent.{Future, Promise}

/**
  * Read the log to determine the last timestamp and number of times it appears.
  *
  * The stream may be terminated at any point. If we didn't do this,
  * we'd potentially have duplicate messages, and the chance that these
  * duplicates happen is quite high.
  */
case class LastLogTime(timeString: String, recordsAtTime: Int) {
  def accept(newTimeString: String): LastLogTime = {
    if (timeString == newTimeString) {
      copy(recordsAtTime = recordsAtTime + 1)
    } else LastLogTime(timeString = newTimeString, recordsAtTime = 1)
  }
}

object LastLogTime {
  def fromFile(file: String): Future[Option[LastLogTime]] = {
    val readInterface = nodejs.readline.Readline.createInterface(
      new ReadlineOptions(
        input = nodejs.fs.Fs.createReadStream(file)
      ))
    val promise = Promise[Option[LastLogTime]]
    var haveLogTime = Option.empty[LastLogTime]
    readInterface.onLine { line =>
      val timeString = line.split("\t").head
      haveLogTime = haveLogTime.map(_.accept(timeString)).orElse(Some(LastLogTime(timeString, 1)))
    }
    readInterface.onClose(() => {
      promise.success(haveLogTime)
    })
    promise.future
  }
}
