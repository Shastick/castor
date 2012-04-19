package digester.input

import scala.actors.Actor

/**
 * A Message Input symbolizes a syslog message source (most probably, from a UDP socket),
 * under the current model, it is the message Input that holds the whole processing path
 * of a syslog message together.
 */
trait MsgInput extends Actor{

	/**
	 * http://www.faqs.org/rfcs/rfc3164.html for details about the structure of a 
	 * UDP syslog packet.
	 * 
	 * 3 mains parts :
	 *  - PRI
	 *  - HEADER
	 *  - MSG
	 */
  
}