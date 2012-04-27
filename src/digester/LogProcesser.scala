package digester

import java.security.KeyStore
import util.messages.SyslogMsg
import util.Stringifier
import util.messages.HashState
import util.messages.AdminMsg

/**
 * Superclass for any log processor. This class chooses what part
 * of syslog messages should be encrypted and hands them down to the Processer implementations.
 */

abstract class LogProcesser(next: LogHandler) extends LogHandler{

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
  def crunchDgram(in: SyslogMsg):SyslogMsg =
    new SyslogMsg(	in.pri,
					in.tstamp,
					crunchArray(in.host),
					crunchArray(in.msg))
}