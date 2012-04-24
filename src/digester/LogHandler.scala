package digester
import util.SyslogMsg
import scala.actors.Actor
import util.AdminMsg

/**
 * LogHandler trait: class to group anything able 
 * to handle a Datagram, should it be for signing, encrypting or writing.
 * 
 * It is meant to permit the chaining of several LogProcessers and to end the
 * chain with a LogFileWriter (who also extends LogHandler)
 */
trait LogHandler extends Actor {
	/**
	 * Defines what is done to the message
	 */
	def procDgram(dg: SyslogMsg)
	def procMsg(m: AdminMsg)
	
	/**
	 * The logHandler's Act() manages the message dispatch.
	 * This should ensure we have no concurrency problems as an Actor
	 * handles one message at a time.
	 */
	def act() = while(true) receive {
	    case m: SyslogMsg => procDgram(m)
	    case m: AdminMsg => procMsg(m)
	}
}