package processer
import scala.actors.Actor
import util.messages.SyslogMsg
import util.messages.AdminMsg
import util.BASE64

/**
 * Handler trait: class to group anything able 
 * to handle a Datagram, should it be for signing, encrypting, writing or whatever.
 * 
 * It is meant to permit the chaining of several Processers and to end the
 * chain with a LogFileWriter (who also extends Handler)
 */
trait Handler extends Actor {
	
  /**
   * set a base 64 converter
   */
	lazy val conv = BASE64.getConverter
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
		react {
	    	case m: SyslogMsg => procDgram(m)
	    	case m: AdminMsg => procAdminMsg(m)
		}
	}
}