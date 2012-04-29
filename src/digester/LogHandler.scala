package digester
import util.messages.SyslogMsg
import scala.actors.Actor
import util.messages.AdminMsg
import util.messages.Comment
import util.messages.SaveState

/**
 * LogHandler trait: class to group anything able 
 * to handle a Datagram, should it be for signing, encrypting or writing.
 * 
 * It is meant to permit the chaining of several LogProcessers and to end the
 * chain with a LogFileWriter (who also extends LogHandler)
 */
trait LogHandler extends Actor {
	/**
	 * Defines what is done to the message(s)
	 */
	def procDgram(dg: SyslogMsg)
	def procAdminMsg(m: AdminMsg)
	
	/**
	 * The logHandler's Act() manages the message dispatch.
	 * This should ensure we have no concurrency problems as an Actor
	 * handles one message at a time.
	 */
	def act() = loop {
		receive {
	    	case m: SyslogMsg => procDgram(m)
	    	case m: AdminMsg => procAdminMsg(m)
		}
	}
	
	/**
	 * TODO : Clear out strange behavior => case class that extends a normal class is not matched
	 * if m: SuperClass is mentioned in the case, but if case CaseClass is specified, it does.
	 */
}