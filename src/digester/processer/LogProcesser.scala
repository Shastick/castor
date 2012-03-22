package digester.processer

import java.security.KeyStore
import util.SyslogMsg
import util.SyslogHeader

/**
 * Superclass for any log processor. This class chooses what part
 * of syslog messages should be encrypted and hands them down to the Processer implementations.
 */

abstract class LogProcesser(ks: KeyStore)  {
  
  def crunchArray(in: Array[Byte]):Array[Byte]
  
  def crunchDGram(in: SyslogMsg):SyslogMsg = {
    // Now encrypting host and message only.
    // TODO : do this based on a config file or whatever config mean.
	val pri = Left(in.pri.left.get)
	val tstamp = Left(in.header.tstamp.left.get)
	val host = Right(crunchArray(in.header.tstamp.left.get.getBytes))
	val msg = Right(crunchArray(in.msg.left.get.getBytes))
    
	new SyslogMsg(pri, new SyslogHeader(tstamp,host),msg)
  }
  
}