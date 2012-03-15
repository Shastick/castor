package util
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import java.security.KeyStore

object AESCipher {
  	val cipher_def = "AES"
	val block_size = 256
			
	def initCipher(ks: KeyStore,k_alias: String, k_pass:String):Cipher={
	  //TODO @julien check if something like AES/CBC/padding scheme can be used here

			val key = ks.getKey(k_alias,k_pass.toCharArray())
			if (key.getEncoded().size < block_size/8)
				throw new Exception("Symmetric AES key too small!")

			val keySpec = new SecretKeySpec(key.getEncoded(),cipher_def)
  
			val cipher = Cipher.getInstance(cipher_def)
			cipher.init(Cipher.ENCRYPT_MODE,keySpec)
			cipher
	}
}