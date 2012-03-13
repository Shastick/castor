package digester.processer
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import java.security.KeyStore
import java.io.FileInputStream


class AESProcesser extends LogProcesser {
  //TODO @julien check if something like AES/CBC/padding scheme can be used here
  val cipher_def = "AES"
  val block_size = 256
  
  val key_alias = "aes_pony"
  val key_pass = ""
  
  val keystore = KeyStore.getInstance("JKS")
  keystore.load(new FileInputStream("keystore"), null)
  val key = keystore.getKey(key_alias,key_pass.toCharArray())
  val cipher = Cipher.getInstance(cipher_def)
  cipher.init(Cipher.ENCRYPT_MODE,key)

  /**
   * Encrypt a byte array
   * Currently EVERYTHING is encrypted, even if the bytes represent a one character String...
   * TODO @julien => think about it => good or bad ?
   */
  def crunchLine(input: Array[Byte]):Array[Byte]={
    cipher.doFinal(input)
  }
  
}