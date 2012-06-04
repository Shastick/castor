package util.cipher
import javax.crypto.Cipher
import java.security.Security
import java.security.Key
import org.bouncycastle.jce.provider.BouncyCastleProvider

class RSACipher(cipher: Cipher){

  def crunchArray(bytes: Array[Byte]):Array[Byte]={
	  val out = new Array[Byte](cipher.getOutputSize(bytes.size))
	  val in_data = bytes.grouped(cipher.getBlockSize())
	  val proc_data = in_data.flatMap(b => cipher.doFinal(b))
	  proc_data.copyToArray(out)
	  out
	}
} 

object RSACipher {
  
  		val cipher_def = "RSA/ECB/PKCS1PADDING"
		val provider = "BC"
		Security.addProvider(new BouncyCastleProvider())
		
	def init(pk: Key, mode: Int): RSACipher = {	
  		val cipher = Cipher.getInstance(cipher_def,provider) 
  		cipher.init(mode, pk)
  		new RSACipher(cipher)
	}
}