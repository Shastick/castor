package processer.crypt
import util.cipher.AESCipher
import java.security.Key
import processer.Processer
import processer.Handler
import util.messages.SyslogMsg
import util.messages.ClearSyslogMsg
import util.messages.CipherSyslogMsg
import util.messages.Message
import util.Stringifier
import java.security.MessageDigest

class AESProcesser(next: Handler, k: Key , mode: Int) 
extends Processer(next) {
	
  val cipher = AESCipher.init(k,mode)
  val digest = MessageDigest.getInstance("SHA-512")
  
  override def crunchDgram(m: SyslogMsg): Message = {
    val iv = makeIV(m)
    m match {
	    case m: ClearSyslogMsg =>
	      new CipherSyslogMsg(Left(m.pri),
	    		  			Left(m.tstamp),
	    		  			Right(cipher.crunchArray(iv, Stringifier(m.host))),
	    		  			Right(cipher.crunchArray(iv, Stringifier(m.msg))))
	    case m: CipherSyslogMsg =>
	      new ClearSyslogMsg(get(iv, m.pri),
	    		  			get(iv, m.tstamp),
	    		  			get(iv, m.host),
	    		  			get(iv, m.msg))  
  	}
  }
  
  /**
   * Take the string in an Either or Decrypt the array and convert it to a string.
   */
  private def get(iv: Array[Byte], e: Either[String,Array[Byte]]): String = 
    if(e.isLeft) e.left.get
    else Stringifier(cipher.crunchArray(iv, e.right.get))
    
  def makeIV(m: SyslogMsg) = {
    val toHash = m match {
      case m: ClearSyslogMsg => Stringifier(m.tstamp)
	  case m: CipherSyslogMsg => Stringifier(m.tstamp.left.get)
    }
    digest.reset
    truncate(digest.digest(toHash))
  }
  /**
   * Make sure the IV is 16 bytes long.
   */
  private def truncate(in: Array[Byte]) = {
    var o = (Array.fill[Byte](16)((new java.lang.Integer(-1)).toByte) _).toArray
    in.copyToArray(o,0,15)
    o
  }
}