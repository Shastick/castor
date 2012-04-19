package digester
import util.SyslogMsg

/**
 * LogHandler trait: class to group anything able 
 * to handle a Datagram, should it be for signing, encrypting or writing.
 * 
 * It is meant to permit the chaining of several LogProcessers and to end the
 * chain with a LogFileWriter (who also extends LogHandler)
 */
trait LogHandler {
	/**
	 * Defines what is done to the message
	 */
	def procDgram(dg: SyslogMsg)
}