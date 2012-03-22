package digester.processer
import util.ClrSyslogMsg
import util.CiphSyslogMsg
import util.SyslogHeader
import java.security.KeyStore


abstract class LogProcesser(ks: KeyStore)  {
  
  def crunchArray(in: Array[Byte]):Array[Byte]
  
  def crunchDGram(in: ClrSyslogMsg):CiphSyslogMsg = {
		  val lol = in.pri
    null
  }
  
}