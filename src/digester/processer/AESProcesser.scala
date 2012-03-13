package digester.processer
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.SecretKeyEntry
import java.security.KeyStore.PasswordProtection


class AESProcesser extends LogProcesser {
  //TODO @julien check if something like AES/CBC/padding scheme can be used here
  val cipher_def = "AES"
  val block_size = 256
  val keystore_loc = "keystore"
  
  val key_alias = "aes_pony1"
  val key_pass = ""
  val keystore = loadKeystore(keystore_loc)
  val key = keystore.getKey(key_alias,key_pass.toCharArray())
  if (key.getEncoded().size < block_size) throw new Exception("Symmetric AES key too small!")
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
  
  def resetKey(){
    val kgen = KeyGenerator.getInstance("AES");
    kgen.init(block_size); // 192 and 256 bits may not be available
    val skey = kgen.generateKey();
    val skeyEntry = new SecretKeyEntry(skey)
    
    val keystore = loadKeystore(keystore_loc)
    keystore.deleteEntry(key_alias)
    keystore.setEntry(key_alias,skeyEntry,new PasswordProtection(key_pass.toCharArray()))
  }
  
  private def loadKeystore(file: String):KeyStore = {
    val keystore = KeyStore.getInstance("JCEKS")
    keystore.load(new FileInputStream(file), null)
    keystore
  }
  
}