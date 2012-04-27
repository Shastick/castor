package util.messages

import scala.util.matching.Regex
import util.BASE64
import util.Stringifier

/**
 * Represents a syslog message, either in a cleartext form, or in an
 * encrypted form, using scala's Either class.
 * 
 * Left is cleartext (as a string)
 * Right is ciphertext (as a Byte Array)
 */

class SyslogMsg( 
    val pri: Array[Byte]
    ,val tstamp: Array[Byte]
    ,val host: Array[Byte]
    ,val msg: Array[Byte])
    extends Message {
  
  /**
   * Constructor using strings :
   */
  def this(pri: String, tstamp: String, host: String, msg: String) =
    this(Stringifier(pri),Stringifier(tstamp),Stringifier(host),Stringifier(msg))
  
  /**
   * String conversion shortcuts
   */
  private def ts(a: Array[Byte]): String = Stringifier(a)
  
  override def toString = 	"<" + ts(pri) + ">" +
		  					ts(tstamp) + " " +
		  					ts(host) + " " +
		  					ts(msg)
  
  /**
   * The whole byte representation has to contain the spaces and brackets present in the string
   */
  def toBytes = Stringifier(toString)

}