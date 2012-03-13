package digester.processer
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.SecretKeyEntry
import java.security.KeyStore.PasswordProtection


class AESProcesser(ks: KeyStore) extends ManagedKey(ks) with LogProcesser {
  //TODO @julien check if something like AES/CBC/padding scheme can be used here
  val cipher_def = "AES"
  val block_size = 256
  val keystore_loc = "keystore"
  
  val key_alias = "pony_key"
  val key_pass = ""

  val key = ks.getKey(key_alias,key_pass.toCharArray())
  if (key.getEncoded().size < block_size/8)
	  throw new Exception("Symmetric AES key too small!")

  val keySpec = new SecretKeySpec(key.getEncoded(),cipher_def)
  
  val cipher = Cipher.getInstance(cipher_def)
  cipher.init(Cipher.ENCRYPT_MODE,keySpec)

  /**
   * Encrypt a byte array
   * Currently EVERYTHING is encrypted, even if the bytes represent a one character String...
   * TODO @julien => think about it => good or bad ?
   */
  def crunchLine(input: Array[Byte]):Array[Byte]={
    cipher.doFinal(input)
  } 
}