package digester.reader

import scala.actors.Actor

trait MsgReader extends Actor{

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