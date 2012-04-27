package util.messages

import scala.util.matching.Regex
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
  def toString
}

class ClearSyslogMsg( 
    val pri: String
    ,val tstamp: String
    ,val host: String
    ,val msg: String)
    extends SyslogMsg {
  
  def toString = 	"<" + pri + ">" +
		  			tstamp + " " +
		  			host + " " +
		  			msg
 
  def toBytes = Stringifier(toString)

}

class CipherSyslogMsg(
    val pri: Either[String,Array[Byte]],
    val tstamp: Either[String,Array[Byte]],
    val host: Either[String,Array[Byte]],
    val msg: Either[String,Array[Byte]])
    extends SyslogMsg {
  
  def toString =
  
  def toBytes = 
}