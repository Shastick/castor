package util.messages

import util.BASE64
import util.Stringifier

/**
 * Object handling the parsing of strings to Messages. 
 */

object Parser {

  val conv = BASE64.getConverter
	
  /**
   * fromInput regexp's
   */
  
  private val tstamp_BSD = """\D{3}\s[\d\s]\d\s\d{2}:\d{2}:\d{2}"""
  private val tstamp_sys = """\d{4}-\d{2}-\d{2}[\sT]\d\d:\d\d:\d\d\.\d{2,6}([+-]\d\d:\d\d)?Z?"""
  private val tstamp = """(""" + tstamp_BSD + """|""" + tstamp_sys + """)"""
  private val clr_txt = ("""^<(\d{1,3})>""" + tstamp + """\s(\S*)\s(.*)$""").r
  
  /**
   * Cipher text parsing regexps
   *  - paranoid : every field is crypted together (one big array)
   *  - full :  only the PRIORITY field is in cleartext
   *  - event : HOST and MSG only are crypted
   *  - content : only the MSG is crypted
   *  - timeless : only the TIMESTAMP is crypted
   *  - anonymous : only the HOST is crypted
   *  - blind : TIMESTAMP and HOST are crypted
   */
  
  private val c_paranoid = """^([A-Za-z0-9+/=]*)$""".r
  private val c_full = """^<(\d{1,3})>(\S*)\s(\S*)\s(\S*)$""".r
  private val c_event = ("""^<(\d{1,3})>""" + tstamp + """\s(\S*)\s(\S*)$""").r
  private val c_content = ("""^<(\d{1,3})>""" + tstamp + """\s(\S*)\s(\S*)$""").r
  private val c_timeless = """^<(\d{1,3})>(\S*)\s(\S*)\s(.*)$""".r
  private val c_anonymous = ("""^<(\d{1,3})>""" + tstamp + """\s(\S*)\s(.*)$""").r
  private val c_blind = """^<(\d{1,3})>(\S*)\s(\S*)\s(.*)$""".r
  
  /**
   * HashStates regexp's :
   */
   
  private val m_iba = """^([A-Za-z0-9+/=]*):([A-Za-z0-9+/=]*):([A-Za-z0-9+/=]*):([A-Za-z0-9+/=]*):(\S*)$""".r
  private val m_hmac = """^([A-Za-z0-9+/=]*):([A-Za-z0-9+/=]*):([A-Za-z0-9+/=]*)$""".r
  
  /**
   * Comment regexp :
   */
  private val notification = """^===== NOTIFICATION: (.*) =====$""".r
  private val header = """^===== (.*) =====$""".r
  /**
   * The SyslogParser takes a syslog datagram (in a string formt) as input and parses
   * it into a clear text SyslogMsg.
   * 
   * a datagram has the form
   * <PRIORITY>TIMESTAMP HOST MSG
   */

  def fromInput(dgram: String): Message = dgram match {
    case clr_txt(pri,tstamp,_,host,msg) =>
      	new ClearSyslogMsg(pri, tstamp, host,msg)
    case _ => MalformedSyslogInput(dgram)
  }
  
  /**
   * Parse the strings coming from a digested log (that could include various Admin messages too).
   */
  def fromLog(line: String): Message = line match {
    	case c_event(pri,tstamp,_,host,msg) => makeEvent(pri,tstamp,host,msg)
    	
    	case clr_txt(pri,tstamp,_,host,msg) => new ClearSyslogMsg(pri,tstamp,host,msg)
		
		case m_iba(id,h,s,t,kid) =>  new IBHashState(id,h,s,t,kid) 
		case m_hmac(id,h,s) => new HMACState(id,h,s)
		
		case notification(n) => Notification(n)
		case header(m) => Header(m)
		
		case c_paranoid(m) => new FullCipherText(conv.dec(m))
	  	case _ => throw new Exception("Parse error : " + line)
  }
  
  private def makeEvent(pri: String, t: String, h: String, m: String): SyslogMsg = 
    new CipherSyslogMsg(Left(pri),
    					Left(t),
    					Right(conv.dec(h)),
    					Right(conv.dec(m)))
}