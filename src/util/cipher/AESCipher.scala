package util.cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import java.security.KeyStore
import scala.util.Random
import javax.crypto.spec.IvParameterSpec

import org.bouncycastle.jce.provider.BouncyCastleProvider


class AESCipher(cipher: Cipher) extends LogCipher{
	
    def crunchArray(input: Array[Byte]):Array[Byte]={
	  val ct = new Array[Byte](cipher.getOutputSize(input.size))
	  val ct_len = cipher.update(input,0,input.size,ct,0)
	  cipher.doFinal(ct,ct_len)
	  ct
    } 
}

object AESCipher {
  /**
   * TODO clear out the IV stuff
   */
	val keyAlg_def = "AES"
  	val cipher_def = "AES/CBC/PKCS7PADDING"
  	val provider = "BC"
	val block_size = 256
	val iv = "LOLOLOLOLOLOLOLO".getBytes()
	
	java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider())
			
	def initEncryptionCipher(ks: KeyStore,k_alias: String, k_pass:String):AESCipher={
			val keySpec = getKeySpec(ks,k_alias,k_pass)
			val cipher = Cipher.getInstance(cipher_def, provider)
			cipher.init(Cipher.ENCRYPT_MODE,keySpec, new IvParameterSpec(iv))
			new AESCipher(cipher)
	}
  	
  	def initDecryptionCipher(ks: KeyStore,k_alias: String, k_pass:String):AESCipher={
			val keySpec = getKeySpec(ks,k_alias,k_pass)
			val cipher = Cipher.getInstance(cipher_def, provider)
			cipher.init(Cipher.DECRYPT_MODE,keySpec, new IvParameterSpec(iv))
			new AESCipher(cipher)
	}
  	
  	private def getKeySpec(ks: KeyStore, k_alias: String, k_pass: String):SecretKeySpec = {
  	  val key = ks.getKey(k_alias,k_pass.toCharArray())
  		if (key.getEncoded().size < block_size/8)
  			throw new Exception("AES key too small!")
  	  new SecretKeySpec(key.getEncoded(),keyAlg_def)
  	}
}