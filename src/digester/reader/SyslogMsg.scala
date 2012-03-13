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
    //val regex_string = """(^<\d{1,3}>)(\D{3}\s[\d\s]{2}:\d{2}:\d{2}:\d{2})\s(.*)\s([.\s]*)$"""
		 
    val reg = """.*(\d\d).*$""".r
    
    val str = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(packet)))
    val line = str.readLine()
    val reg(num) = line
    println(num)
    
    SyslogMsg("Empty !",SyslogHeader("",""),"")
	/**val parse = """(\d)""".r
    val dgram = new String(packet)

	println(dgram)
    dgram match {
    case parse(d) => SyslogMsg(d,SyslogHeader("",""),"")
      case _ => SyslogMsg("Empty !",SyslogHeader("",""),"")
      //TODO @julien handle this correctly
    }
    **/
  }
}