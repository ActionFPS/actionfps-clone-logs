import io.scalajs.nodejs.fs

import scala.concurrent.Future

/**
  * Created by william on 10/5/17.
  */
case class AtEventFile(targetFile: String) {

  def appendLine(line: String): Unit = {
    fs.Fs.appendFileSync(targetFile, line + "\n")
  }

  def getLastLogTime: Future[Option[LastLogTime]] = LastLogTime.fromFile(targetFile)

}
