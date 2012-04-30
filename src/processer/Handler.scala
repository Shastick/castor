package processer
import scala.actors.Actor
import util.messages.SyslogMsg
import util.messages.AdminMsg

/**
 * Handler trait: class to group anything able 
 * to handle a Datagram, should it be for signing, encrypting, writing or whatever.
 * 
 * It is meant to permit the chaining of several Processers and to end the
 * chain with a LogFileWriter (who also extends Handler)
 */
trait Handler extends Actor {
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