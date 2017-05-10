import io.scalajs.nodejs
import org.scalatest.AsyncFreeSpec
import org.scalatest.OptionValues._

import scala.concurrent.ExecutionContextExecutor

class AfLogSyncSpec extends AsyncFreeSpec {
  implicit override def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  "it works" in {
    val sampleTimes = List(1, 2, 3, 3, 3).map { n =>
      s"2017-01-02T01:02:0${n}Z"
    }

    val lines = sampleTimes.map { time =>
      s"$time\tserver\tMessage"
    }

    val targetFile = "target/test-file"
    nodejs.fs.Fs.writeFileSync(targetFile, lines.map(_ + "\n").mkString)
    ReadLastTime.getLastTimes(targetFile).map { result =>
      assert(result.value._1 == sampleTimes.last)
      assert(result.value._2 == 3)
    }
  }
}
