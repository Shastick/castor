package util
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import java.security.KeyStore

object AESCipher {
  /**TODO check if something like AES/CBC/padding scheme can be used here => for now,
   * two same datagrams have the exact same ciphertext
   */
  	val cipher_def = "AES"
	val block_size = 256
			
	def initEncryptionCipher(ks: KeyStore,k_alias: String, k_pass:String):Cipher={
			val keySpec = getKeySpec(ks,k_alias,k_pass)  
			val cipher = Cipher.getInstance(cipher_def)
			cipher.init(Cipher.ENCRYPT_MODE,keySpec)
			cipher
	}
  	
  	def initDecryptionCipher(ks: KeyStore,k_alias: String, k_pass:String):Cipher={
			val keySpec = getKeySpec(ks,k_alias,k_pass)
			val cipher = Cipher.getInstance(cipher_def)
			cipher.init(Cipher.DECRYPT_MODE,keySpec)
			cipher
	}
  	
  	private def getKeySpec(ks: KeyStore, k_alias: String, k_pass: String):SecretKeySpec = {
  	  val key = ks.getKey(k_alias,k_pass.toCharArray())
  		if (key.getEncoded().size < block_size/8)
  			throw new Exception("Symmetric AES key too small!")
  	  new SecretKeySpec(key.getEncoded(),cipher_def)
  	}
}