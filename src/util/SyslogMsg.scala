package digester.util
import scala.util.matching.Regex
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.InputStream
import java.io.ByteArrayInputStream

class SyslogMsg(pri: String, header: SyslogHeader, msg: String){
  override def toString() =
    pri + header.timestamp + " " + header.hostname + " " + msg
}

case class SyslogHeader(timestamp: String, hostname: String)

/**
 * ClrSyslogMsg represents a syslog message with all its elements as cleartexts.
 * 
 * Syslog datagram format
 * (PRI)(TIMESTAMP\sHOSTNAME)\s(MSG)
 */
object SyslogParser {
  def apply(datagram: String):SyslogMsg = {
    
    val parse = """^(<\d{1,3}>)(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(.*)""".r
		 
    datagram match {
    case parse(pri,tstamp,host,msg) => new SyslogMsg(pri,SyslogHeader(tstamp,host),msg)
      case _ => throw new Exception("Parser Error : " + datagram)
      //TODO @julien handle this correctly
    }
  }
}