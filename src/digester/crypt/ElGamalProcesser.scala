package digester.crypt
import util.cipher.ElGamalCipher
import util.SyslogMsg 
import digester.LogHandler

class ElGamalProcesser(next: LogHandler) extends LogProcesser(next) {

  val cipher = ElGamalCipher.initEncryptionCiher()
  
  def crunchArray(in: Array[Byte]): Array[Byte] = cipher.crunchArray(in)
}