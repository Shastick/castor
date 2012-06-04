package util.cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import java.security.Security
import java.security.Key
import org.bouncycastle.jce.provider.BouncyCastleProvider
import javax.crypto.spec.IvParameterSpec

class AESCipher(cipher: Cipher, mode: Int, keySpec: SecretKeySpec) {
  
	def crunchArray(iv: Array[Byte], input: Array[Byte]):Array[Byte]={
	  cipher.init(mode, keySpec, new IvParameterSpec(iv))
	  val o = new Array[Byte](cipher.getOutputSize(input.size))
	  val o_len = cipher.update(input,0,input.size,o,0)
	  cipher.doFinal(o,o_len)
	  o
    } 
}

object AESCipher {
  
	val keyAlg_def = "AES"
  	val cipher_def = "AES/CBC/PKCS7PADDING"
  	val provider = "BC"
  	Security.addProvider(new BouncyCastleProvider())
  	
	val block_size = 256
			
	def init(k: Key, mode: Int):AESCipher={
			val keySpec = makeKeySpec(k)
			val cipher = Cipher.getInstance(cipher_def, provider)
			new AESCipher(cipher, mode, keySpec)
	}
  	
  	private def makeKeySpec(k: Key):SecretKeySpec = {
  		if (k.getEncoded().size < block_size/8)
  			throw new Exception("AES key too small!")
  	  new SecretKeySpec(k.getEncoded(),keyAlg_def)
  	}
}