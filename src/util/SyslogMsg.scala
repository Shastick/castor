package util
import scala.util.matching.Regex
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.InputStream
import java.io.ByteArrayInputStream

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
    ,val msg: Either[String,Array[Byte]]){
  
  override def toString() = "<" + ets(pri) + ">" +
		  					ets(header.tstamp) + " " +
		  					ets(header.host) + " " +
		  					ets(msg)
  
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
    

/**
 * The SyslogParser takes a syslog datagram (in a string formt) as input and parses
 * it into a clear text SyslogMsg.
 * 
 * a datagram has the form
 * <PRIORITY>TIMESTAMP HOST MSG
 */
    
object SyslogParser {
  def apply(datagram: String):SyslogMsg = {
    //TODO timestamp regexp probably doesn't cover all possible timestamp forms
    val parse = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(.*)""".r
		 
    datagram match {
    case parse(pri,tstamp,host,msg) => 
      	new SyslogMsg(Left(pri), new SyslogHeader(Left(tstamp),Left(host)),Left(msg))
    case _ => throw new Exception("Parser Error : " + datagram)
      //TODO @julien handle this correctly
    }
  }
}