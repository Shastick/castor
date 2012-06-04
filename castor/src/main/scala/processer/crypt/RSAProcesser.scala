package processer.crypt
import util.cipher.RSACipher
import java.security.Key
import processer.Handler
import processer.Processer
import util.messages.SyslogMsg
import util.messages.ClearSyslogMsg
import util.messages.CipherSyslogMsg
import util.messages.Message
import util.Stringifier

class RSAProcesser(next: Handler, k: Key , mode: Int) extends Processer(next) {
	
	val cipher = RSACipher.init(k,mode) 
	
	def crunchArray(bytes: Array[Byte]): Array[Byte] = cipher.crunchArray(bytes)
	
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