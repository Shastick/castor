package digester.processer
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator


class AESProcesser extends LogProcesser {
  
  //TODO @julien check if something like AES/CBC/padding scheme can be used here
  val cipher_def = "AES"
  val block_size = 256
  val keygen = KeyGenerator.getInstance(cipher_def)
  
  keygen.init(block_size)
  val key = keygen.generateKey()
  val byte_key = key.getEncoded()
    
  val keyspec = new SecretKeySpec(byte_key, cipher_def)
  val cipher = Cipher.getInstance(cipher_def)
  cipher.init(Cipher.ENCRYPT_MODE,keyspec)

  /**
   * Encrypt a byte array
   * Currently EVERYTHING is encrypted, even if the bytes represent a one character String...
   * TODO @julien => think about it => good or bad ?
   */
  def crunchLine(input: Array[Byte]):Array[Byte]={
    cipher.doFinal(input)
  }
  
}