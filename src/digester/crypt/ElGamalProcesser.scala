package digester.processer
import util.cipher.ElGamalCipher
import util.SyslogMsg
import digester.writer.LineWriter
import digester.LogHandler

class ElGamalProcesser(next: LogHandler) extends LogProcesser(next) {

  val cipher = ElGamalCipher.initEncryptionCiher()
  
  def crunchArray(in: Array[Byte]): Array[Byte] = cipher.crunchArray(in)
}