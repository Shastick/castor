package util.cipher
import javax.crypto.Cipher
import java.security.KeyStore
import java.security.cert.X509Certificate
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.security.PublicKey
import java.security.PrivateKey
import java.security.Key

class RSACipher(cipher: Cipher) extends LogCipher {
  	
  override def crunchArray(bytes: Array[Byte]):Array[Byte]={
	  val out = new Array[Byte](cipher.getOutputSize(bytes.size))
	  val in_data = bytes.grouped(cipher.getBlockSize())
	  val proc_data = in_data.flatMap(b => cipher.doFinal(b))
	  proc_data.copyToArray(out)
	  out
	}
} 

//TODO : store only a private or a public key in the keystore.
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