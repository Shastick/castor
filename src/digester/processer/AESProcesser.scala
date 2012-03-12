package digester.processer
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher


class AESProcesser extends LogProcesser {
  
  val cipher_name = "AES"
  
  val keyspec = new SecretKeySpec("trolilol".getBytes() , cipher_name)
  val cipher = Cipher.getInstance(cipher_name)
  cipher.init(Cipher.ENCRYPT_MODE,keyspec)
  
  
  
  def crunchLine(input: Array[Byte]) {
    val encrypted = cipher.doFinal(input)
  }
  
}