package processer
import util.messages.AdminMsg
import util.messages.ClearSyslogMsg
import util.Stringifier
import util.messages.CipherSyslogMsg
import util.messages.SyslogMsg
import util.messages.Message

/**
 * Superclass for any log processor. This class chooses what part
 * of syslog messages should be encrypted and hands them down to the Processer implementations.
 */

abstract class Processer(next: Handler) extends Handler{

  def procDgram(m: SyslogMsg) = next ! crunchDgram(m)
  
  /**
   * How an individual byte array is processed
   */
  def crunchArray(in: Array[Byte]):Array[Byte]
  
  /**
   * Default behavior for non-datagram messages :
   * propagate them without modification.
   */
  def procAdminMsg(m: AdminMsg) = next ! m 
  
  /**
   * How a whole datagram is processed.
   * Now only processing host and message
   * TODO : DO THIS BASED ON A CONFIG MEAN
   */
  def crunchDgram(m: SyslogMsg): Message = m match {
    case m: ClearSyslogMsg =>
      new CipherSyslogMsg(Left(m.pri),
    		  			Left(m.tstamp),
    		  			Right(crunchArray(Stringifier(m.host))),
    		  			Right(crunchArray(Stringifier(m.msg))))
    case m: CipherSyslogMsg =>
      new ClearSyslogMsg(get(m.pri),
    		  			get(m.tstamp),
    		  			get(m.host),
    		  			get(m.msg))
  }
  
  /**
   * Take the string in an Either or Decrypt the array and convert it to a string.
   */
  private def get(e: Either[String,Array[Byte]]): String = 
    if(e.isLeft) e.left.get
    else Stringifier(crunchArray(e.right.get))
    
}