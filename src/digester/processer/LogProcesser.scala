package digester.processer

import java.security.KeyStore
import util.SyslogMsg
import util.SyslogHeader
import util.Stringifier
import digester.writer.LineWriter

/**
 * Superclass for any log processor. This class chooses what part
 * of syslog messages should be encrypted and hands them down to the Processer implementations.
 */

abstract class LogProcesser(lw: LineWriter) {
  
  def crunchArray(in: Array[Byte]):Array[Byte]

  def procDgram(m: SyslogMsg) = lw.writeDgram(crunchDgram(m))
  
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