package digester.processer
import util.cipher.ElGamalCipher
import util.SyslogMsg
import digester.writer.LineWriter

class ElGamalProcesser(lw: LineWriter) extends LogProcesser(lw) {

  val cipher = ElGamalCipher.initEncryptionCiher()
  
  def crunchArray(in: Array[Byte]): Array[Byte] = cipher.crunchArray(in)
  def writeDgram(m: SyslogMsg) = lw.writeDgram(m)
}