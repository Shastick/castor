package processer.output
import util.messages.SyslogMsg
import util.messages.AdminMsg
import processer.Handler
import util.messages.HashError

class Screen extends Handler{
	def procDgram(dg: SyslogMsg) = println(dg.toString)
	def procAdminMsg(m: AdminMsg) = println(m.toString)
}

class AdminScreen extends Screen {
  override def procDgram(dg: SyslogMsg) = Unit
}

class ErrorScreen extends Screen {
  override def procDgram(dg: SyslogMsg) = Unit
  override def procAdminMsg(m: AdminMsg) = m match {
    case HashError(e) => println(m.toString())
    case _ =>
  }
}