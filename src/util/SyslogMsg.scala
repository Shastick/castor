package util
import scala.util.matching.Regex

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
  
  override def toString = "<"+ets(pri)+">"+ets(header.tstamp)+" "+ets(header.host)+" "+ets(msg)
  
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
    

/**
 * The SyslogParser takes a syslog datagram (in a string formt) as input and parses
 * it into a clear text SyslogMsg.
 * 
 * a datagram has the form
 * <PRIORITY>TIMESTAMP HOST MSG
 */
    
object SyslogParser {
  def dec(s:String) =  BASE64.dec(s)
  /**
   * Clear text parsing regexps
   * TODO => timestamp regexp probably doesn't cover all possible timestamp forms
   */
  val clr_txt = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(.*)$""".r
  /**
   * Cipher text parsing regexps
   *  - paranoid : every field is crypted
   *  - full :  only the PRIORITY field is in cleartext
   *  - event : HOST and MSG only are crypted
   *  - content : only the MSG is crypted
   *  - timeless : only the TIMESTAMP is crypted
   *  - anonymous : only the HOST is crypted
   *  - blind : TIMESTAMP and HIST are crypted
   *  
   *  TODO check if the regexps can indeed correctly differentiate every case,
   *  and in which cases they collide
   */
  val c_paranoid = """^<(\S*)>(\S*)\s(\S*)\s(\S*)$""".r
  val c_full = """^<(\d{1,3})>(\S*)\s(\S*)\s(\S*)$""".r
  val c_event = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(\S*)$""".r
  val c_content = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(\S*)$""".r
  val c_timeless = """^<(\d{1,3})>(\S*)\s(\S*)\s(.*)$""".r
  val c_anonymous = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(.*)$""".r
  val c_blind = """^<(\d{1,3})>(\S*)\s(\S*)\s(.*)$""".r
  
  def parseClearText(datagram: String):SyslogMsg = {
		 
    datagram match {
    case clr_txt(pri,tstamp,host,msg) => 
      	new SyslogMsg(Left(pri), new SyslogHeader(Left(tstamp),Left(host)),Left(msg))
    case _ => throw new Exception("Parser Error : " + datagram)
      //TODO @julien handle this correctly
    }
  }
  
  def parseCipherText(c: String):SyslogMsg = 
	c match {
		case c_event(pri,tstamp,host,msg) =>
					  new SyslogMsg(Left(pri)
					    ,new SyslogHeader(Left(tstamp),Right(dec(host)))
					  	,Right(dec(msg)))
	  	case _ => throw new Exception("Parse error : " + c)
  	}
}