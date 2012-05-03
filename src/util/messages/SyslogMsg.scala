package util.messages
import util.BASE64
import util.Stringifier

/**
 * Syslog Messages are represented by SyslogMsg
 * The Clear variant has only strings,
 * whereas the Cipher variant has eithers => if the either is right,
 * its content is encrypted.
 * If the either is left, the content is cleartex and kept as a String.
 */

abstract class SyslogMsg extends Message {
  def toBytes = Stringifier(toString)
  def toString: String
}

class ClearSyslogMsg( 
    val pri: String
    ,val tstamp: String
    ,val host: String
    ,val msg: String)
    extends SyslogMsg {
  
  override def toString = 	"<" + pri + ">" +
		  			tstamp + " " +
		  			host + " " +
		  			msg
}

class CipherSyslogMsg(
    val pri: Either[String,Array[Byte]],
    val tstamp: Either[String,Array[Byte]],
    val host: Either[String,Array[Byte]],
    val msg: Either[String,Array[Byte]])
    extends SyslogMsg {
  
  override def toString = "<" + ets(pri) + ">" +
				ets(tstamp) + " " +
				ets(host) + " " +
				ets(msg)

  /**
   * ets => Either to String function : takes an either, returns the string if it is one,
   * and converts the bytes to an b64 encoded string if the Either is a byte array.
   */
  private def ets(in: Either[String, Array[Byte]]): String =
    if(in.isLeft) in.left.get
    else BASE64.enc(in.right.get)

}

class FullCipherText(val p: Array[Byte]) extends SyslogMsg {
  
  def this(s: String) = this(BASE64.dec(s))
  
  override def toBytes = p
  override def toString = BASE64.enc(p)
}
case class MalformedSyslogInput(d: String) extends SyslogMsg
case object UnauthorizedAccess extends SyslogMsg