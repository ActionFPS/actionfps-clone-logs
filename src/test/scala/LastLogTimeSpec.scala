import io.scalajs.nodejs
import org.scalatest.AsyncFreeSpec
import org.scalatest.OptionValues._

import scala.concurrent.ExecutionContextExecutor

class LastLogTimeSpec extends AsyncFreeSpec {
  implicit override def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  "it correctly reads the last timestamp and number of records extracted" in {
    val sampleTimes = List(1, 2, 3, 3, 3).map { n =>
      s"2017-01-02T01:02:0${n}Z"
    }

    val lines = sampleTimes.map { time =>
      s"$time\tserver\tMessage"
    }

    val targetFile = "target/test-file"
    nodejs.fs.Fs.writeFileSync(targetFile, lines.map(_ + "\n").mkString)
    LastLogTime.fromFile(targetFile).map { result =>
      assert(result.value.timeString == sampleTimes.last)
      assert(result.value.recordsAtTime == 3)
    }
  }

}
