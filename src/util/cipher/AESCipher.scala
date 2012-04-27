package util.cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import java.security.KeyStore
import scala.util.Random
import javax.crypto.spec.IvParameterSpec
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Key
import java.security.Security


class AESCipher(cipher: Cipher) extends LogCipher{
	def crunchArray(input: Array[Byte]):Array[Byte]={
	  val o = new Array[Byte](cipher.getOutputSize(input.size))
	  val o_len = cipher.update(input,0,input.size,o,0)
	  cipher.doFinal(o,o_len)
	  o
    } 
}

object AESCipher {
  /**
   * TODO clear out the IV stuff
   */
	val keyAlg_def = "AES"
  	val cipher_def = "AES/CBC/PKCS7PADDING"
  	val provider = "BC"
  	Security.addProvider(new BouncyCastleProvider())
  	
	val block_size = 256
	val iv = "LOLOLOLOLOLOLOLO".getBytes()
			
	def init(k: Key, mode: Int):AESCipher={
			val keySpec = makeKeySpec(k)
			val cipher = Cipher.getInstance(cipher_def, provider)
			cipher.init(mode,keySpec, new IvParameterSpec(iv))
			new AESCipher(cipher)
	}
  	
  	private def makeKeySpec(k: Key):SecretKeySpec = {
  		if (k.getEncoded().size < block_size/8)
  			throw new Exception("AES key too small!")
  	  new SecretKeySpec(k.getEncoded(),keyAlg_def)
  	}
}