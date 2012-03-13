package digester.reader
import scala.util.matching.Regex
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.InputStream
import java.io.ByteArrayInputStream

case class SyslogMsg(pri: String, header: SyslogHeader, msg: String)

case class SyslogHeader(timestamp: String, hostname: String)

/**
 * (PRI)(TIMESTAMP\sHOSTNAME)\s(MSG)
 */
object SyslogParser {
  def apply(datagram: String):SyslogMsg = {
    //Raw string with """ """
    //val parse = """^(<\d{1,3}>)(\D{3}\s[\d\s]{2}:\d{2}:\d{2}:\d{2})\s(.*)\s([.\s]*)$""".r
    val parse = """^(<\d{1,3}>)(\D{3}).*""".r
		 
    datagram match {
    case parse(pri,tstamp) => SyslogMsg(pri,SyslogHeader(tstamp,"host"),"msg")
      case _ => SyslogMsg("Empty !",SyslogHeader("",""),"")
      //TODO @julien handle this correctly
    }
  }
}