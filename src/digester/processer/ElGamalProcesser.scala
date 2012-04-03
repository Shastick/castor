package digester.processer
import util.cipher.ElGamalCipher

class ElGamalProcesser extends LogProcesser {

  val cipher = ElGamalCipher.initEncryptionCiher()
  
  def crunchArray(in: Array[Byte]): Array[Byte] = cipher.crunchArray(in)

}