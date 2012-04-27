package util.messages
import util.BASE64
import util.Stringifier

/**
 * Object handling the parsing of strings to Messages. 
 */

object Parser {

  /**
   * fromInput regexp's
   * TODO : check EVERY timestamp format FFS !
   */
  
  private val clr_txt = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(.*)$""".r
  
  /**
   * Cipher text parsing regexps
   *  - paranoid : every field is crypted
   *  - full :  only the PRIORITY field is in cleartext
   *  - event : HOST and MSG only are crypted
   *  - content : only the MSG is crypted
   *  - timeless : only the TIMESTAMP is crypted
   *  - anonymous : only the HOST is crypted
   *  - blind : TIMESTAMP and HOST are crypted
   *  
   *  TODO check if the regexps can indeed correctly differentiate every case,
   *  and in which cases they collide.
   *  AND SELECT ONE BASED ON CONFIG ! ! !
   */
  
  private val c_paranoid = """^<(\S*)>(\S*)\s(\S*)\s(\S*)$""".r
  private val c_full = """^<(\d{1,3})>(\S*)\s(\S*)\s(\S*)$""".r
  private val c_event = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(\S*)$""".r
  private val c_content = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(\S*)$""".r
  private val c_timeless = """^<(\d{1,3})>(\S*)\s(\S*)\s(.*)$""".r
  private val c_anonymous = """^<(\d{1,3})>(\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2})\s(\S*)\s(.*)$""".r
  private val c_blind = """^<(\d{1,3})>(\S*)\s(\S*)\s(.*)$""".r
  
  /**
   * HashStates regexp's :
   */
   
  private val m_iba = """^([A-Za-z0-9+/]*):([A-Za-z0-9+/]*):([A-Za-z0-9+/]*):([A-Za-z0-9+/]*)$""".r
  private val m_hmac = """^([A-Za-z0-9+/]*):([A-Za-z0-9+/]*):([A-Za-z0-9+/]*)$""".r
  
  /**
   * Comment regexp :
   */
  private val comment = """^=====(.*)=====$""".r
  /**
   * The SyslogParser takes a syslog datagram (in a string formt) as input and parses
   * it into a clear text SyslogMsg.
   * 
   * a datagram has the form
   * <PRIORITY>TIMESTAMP HOST MSG
   */
  def fromInput(dgram: String): SyslogMsg = dgram match {
    case clr_txt(pri,tstamp,host,msg) => 
      	new ClearSyslogMsg(pri, tstamp,host,msg)
    case _ => throw new Exception("Parser Error : " + dgram)
      //TODO @julien handle this correctly
  }
  
  /**
   * Parse the strings coming from a digested log (that could include various Admin messages too).
   */
  def fromLog(line: String): Message = line match {
		case c_event(pri,tstamp,host,msg) => makeEvent(pri,tstamp,host,msg)
		case m_iba(id,h,s,t) =>  new IBHashState(id,h,s,t) 
		case m_hmac(id,h,s) => new HashState(id,h,s)
		case comment(m) => new Comment(m)
	  	case _ => throw new Exception("Parse error : " + line)
  }
  
  private def makeEvent(pri: String, t: String, h: String, m: String): SyslogMsg = 
    new CipherSyslogMsg(Left(pri),
    					Left(t),
    					Right(BASE64.dec(h)),
    					Right(BASE64.dec(m)))
  
}