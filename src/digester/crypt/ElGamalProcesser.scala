package digester.crypt
import util.cipher.ElGamalCipher
import util.messages.SyslogMsg
import digester.LogHandler
import digester.LogProcesser

class ElGamalProcesser(next: LogHandler) extends LogProcesser(next) {

  val cipher = ElGamalCipher.initEncryptionCiher()
  
  def crunchArray(in: Array[Byte]): Array[Byte] = cipher.crunchArray(in)
}