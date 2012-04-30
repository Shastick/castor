package processer.output
import util.messages.SyslogMsg
import util.messages.AdminMsg
import processer.Handler

/**
 * Thank you to Matthias Brändli for this AMMMMMMMMMMAZING name idea.
 */

class MessageSync extends Handler{
	def procDgram(dg: SyslogMsg) = println(dg.toString)
	def procAdminMsg(m: AdminMsg) = println(m.toString)
}