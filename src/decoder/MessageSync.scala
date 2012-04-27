package decoder
import digester.LogHandler
import util.messages.SyslogMsg
import util.messages.AdminMsg

/**
 * Thank you to Matthias Br�ndli for this AMMMMMMMMMMAZING name idea.
 */

class MessageSync extends LogHandler{
	def procDgram(dg: SyslogMsg) = println(dg.toString)
	def procAdminMsg(m: AdminMsg) = println(m.toString)
}