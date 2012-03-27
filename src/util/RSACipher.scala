package util
import javax.crypto.Cipher
import java.security.KeyStore
import java.security.cert.X509Certificate
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

object RSACipher {
  
  		val cipher_def = "RSA/ECB/PKCS1PADDING"
		val provider = "BC"
		Security.addProvider(new BouncyCastleProvider())
		
	def initEncryptionCipher(ks: KeyStore, c_alias:String):Cipher={
  		val cert = getCert(ks,c_alias)		
  		val cipher = Cipher.getInstance(cipher_def,provider) 
  		cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey())
  		cipher
	}
  		
  	def initDecryptionCipher(ks: KeyStore,k_alias:String, k_pwd:String):Cipher={
  		val key = ks.getKey(k_alias,k_pwd.toCharArray)
  		val cipher = Cipher.getInstance(cipher_def,provider) 
  		cipher.init(Cipher.DECRYPT_MODE, key)
  		cipher
  	}
  	
  	private def getCert(ks: KeyStore, c_alias: String):X509Certificate = {
		/* Use existing keystore */
  		ks.getCertificate(c_alias) match {
			case c:X509Certificate => c
			case _ => throw new Exception("Woops, not a X509Certificate loaded!")
		}
  	}
}