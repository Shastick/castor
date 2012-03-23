package util
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import java.security.KeyStore
import scala.util.Random
import javax.crypto.spec.IvParameterSpec

import org.bouncycastle.jce.provider.BouncyCastleProvider

object AESCipher {
  /**
   * TODO check if something like AES/CBC/padding scheme can be used here => for now,
   * two same datagrams have the exact same ciphertext
   */
	val keyAlg_def = "AES"
  	val cipher_def = "AES/CBC/PKCS7PADDING"
  	val provider = "BC"
	val block_size = 256
	val iv = "LOLOLOLOLOLOLOLO".getBytes()
	
	java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider())
			
	def initEncryptionCipher(ks: KeyStore,k_alias: String, k_pass:String):Cipher={
			val keySpec = getKeySpec(ks,k_alias,k_pass)
			val cipher = Cipher.getInstance(cipher_def, provider)
			cipher.init(Cipher.ENCRYPT_MODE,keySpec, new IvParameterSpec(iv))
			cipher
	}
  	
  	def initDecryptionCipher(ks: KeyStore,k_alias: String, k_pass:String):Cipher={
			val keySpec = getKeySpec(ks,k_alias,k_pass)
			val cipher = Cipher.getInstance(cipher_def, provider)
			cipher.init(Cipher.DECRYPT_MODE,keySpec, new IvParameterSpec(iv))
			cipher
	}
  	
  	private def getKeySpec(ks: KeyStore, k_alias: String, k_pass: String):SecretKeySpec = {
  	  val key = ks.getKey(k_alias,k_pass.toCharArray())
  		if (key.getEncoded().size < block_size/8)
  			throw new Exception("AES key too small!")
  	  new SecretKeySpec(key.getEncoded(),keyAlg_def)
  	}
}