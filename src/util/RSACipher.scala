package util
import javax.crypto.Cipher
import java.security.KeyStore
import java.security.cert.X509Certificate
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class RSACipher(cipher: Cipher) extends LogCipher {
  	
  def crunchArray(bytes: Array[Byte]):Array[Byte]={
	  val out = new Array[Byte](cipher.getOutputSize(bytes.size))
	  val clear_data = bytes.grouped(cipher.getBlockSize())
	  val cipher_data = clear_data.flatMap(b => cipher.doFinal(b))
	  cipher_data.copyToArray(out)
	  out
	}
} 

object RSACipher {
  
  		val cipher_def = "RSA/ECB/PKCS1PADDING"
		val provider = "BC"
		Security.addProvider(new BouncyCastleProvider())
		
	def initEncryptionCipher(ks: KeyStore, c_alias:String):RSACipher={
  		val cert = getCert(ks,c_alias)		
  		val cipher = Cipher.getInstance(cipher_def,provider) 
  		cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey())
  		new RSACipher(cipher)
	}
  		
  	def initDecryptionCipher(ks: KeyStore,k_alias:String, k_pwd:String):RSACipher={
  		val key = ks.getKey(k_alias,k_pwd.toCharArray)
  		val cipher = Cipher.getInstance(cipher_def,provider) 
  		cipher.init(Cipher.DECRYPT_MODE, key)
  		new RSACipher(cipher)
  	}
  	
  	private def getCert(ks: KeyStore, c_alias: String):X509Certificate = {
		/* Use existing keystore */
  		ks.getCertificate(c_alias) match {
			case c:X509Certificate => c
			case _ => throw new Exception("Woops, not a X509Certificate loaded!")
		}
  	}
}