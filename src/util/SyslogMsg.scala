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
    ,val msg: Either[String,Array[Byte]] )

class SyslogHeader(
    val tstamp: Either[String,Array[Byte]]
    ,val host: Either[String,Array[Byte]])

object SyslogParser {
  def apply(datagram: String):SyslogMsg = {
    
    val parse = """^(<\d{1,3}>)(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(.*)""".r
		 
    datagram match {
    case parse(pri,tstamp,host,msg) => 
      	new SyslogMsg(Left(pri), new SyslogHeader(Left(tstamp),Left(host)),Left(msg))
    case _ => throw new Exception("Parser Error : " + datagram)
      //TODO @julien handle this correctly
    }
  }
}