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
   * Default behavior for non-datagram messages :
   * propagate them without modification.
   */
  def procAdminMsg(m: AdminMsg) = next ! m 
  
  /**
   * How a whole datagram is processed.
   * Now only processing host and message
   * TODO : do this based on a config mean
   */
  def crunchDgram(m: SyslogMsg): Message 
    
}