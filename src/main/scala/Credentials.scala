import io.scalajs.nodejs.process

import scala.scalajs.js

/**
  * Created by william on 12/5/17.
  */
object Credentials {

  private val SiteName = "actionfps.com"

  def loadNetRC(): Option[Credentials] = {
    val nr = new NetRC()
    for {
      site <- nr
        .asInstanceOf[js.Dictionary[js.Dictionary[String]]]
        .get(SiteName)
      password <- site.get("password")
      if !site.contains("login")
    } yield Credentials(authorizationString = s"Bearer $password")
  }

  private val AuthorizationEnv = "AUTHORIZATION"

  def loadEnv(): Option[Credentials] = {
    process.env.get(AuthorizationEnv).map(Credentials.apply)
  }
}

case class Credentials(authorizationString: String)
