package processer.output
import util.messages.SyslogMsg
import util.messages.AdminMsg
import processer.Handler
import util.messages.HashError

/**
 * Screen : output everything to the console
 */
class Screen extends Handler{
	def procDgram(dg: SyslogMsg) = println(dg.toString)
	def procAdminMsg(m: AdminMsg) = println(m.toString)
}

/**
 * AdminScreen : output only Administration messages
 */
class AdminScreen extends Screen {
  override def procDgram(dg: SyslogMsg) = Unit
}

/**
 * ErrorScreen : output only Error messages
 */
class ErrorScreen extends Screen {
  override def procDgram(dg: SyslogMsg) = Unit
  override def procAdminMsg(m: AdminMsg) = m match {
    case e: Error => println(m.toString())
    case _ =>
  }
}