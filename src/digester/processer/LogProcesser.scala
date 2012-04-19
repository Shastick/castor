package digester.processer

import java.security.KeyStore
import util.SyslogMsg
import util.SyslogHeader
import util.Stringifier
import digester.writer.LineWriter
import digester.LogHandler

/**
 * Superclass for any log processor. This class chooses what part
 * of syslog messages should be encrypted and hands them down to the Processer implementations.
 */

abstract class LogProcesser(next: LogHandler) extends LogHandler{

  def procDgram(m: SyslogMsg) = pushDgram(crunchDgram(m))
  
  /**
   * pushDgram sends the datagram to the next logHandler
   */
  def pushDgram(m: SyslogMsg) = next.procDgram(m)
  
  /**
   * How an individual byte array is processed
   */
  def crunchArray(in: Array[Byte]):Array[Byte]
  
  /**
   * How a whole datagram is processed
   */
  def crunchDgram(in: SyslogMsg):SyslogMsg = {
    // Now encrypting host and message only.
    // TODO : do this based on a config file or whatever config mean.
	val pri = Left(in.pri.left.get)
	val tstamp = Left(in.header.tstamp.left.get)
	val host = Right(crunchArray(Stringifier.tb(in.header.host.left.get)))
	val msg = Right(crunchArray(Stringifier.tb(in.msg.left.get)))
    
	new SyslogMsg(pri, new SyslogHeader(tstamp,host),msg)
  }
  
}