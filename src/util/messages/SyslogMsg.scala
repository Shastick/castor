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
    val pri: Either[String,Array[Byte]]
    ,val header: SyslogHeader
    ,val msg: Either[String,Array[Byte]])
    extends Message {
  
  override def toString = 	"<" + ets(pri) + ">" +
		  					ets(header.tstamp) + " " +
		  					ets(header.host) + " " +
		  					ets(msg)
  
  def toBytes = Stringifier.toBytes(toString)
  /**
   * ets => Either to String function : takes an either, returns the string if it is one,
   * and converts the bytes to an encoded string if the Either is a byte array.
   */
  private def ets(in: Either[String, Array[Byte]]): String = in match {
      case l: Left[String, Array[Byte]] => l.left.get
      case r: Right[String, Array[Byte]] => BASE64.enc(r.right.get)
      case _ => throw new Exception("Something went wrong with the SyslogMsg Either's... : " + in)
  }
}

class SyslogHeader(
    val tstamp: Either[String,Array[Byte]]
    ,val host: Either[String,Array[Byte]])