package util
import javax.crypto.Cipher
import java.security.KeyStore
import java.security.cert.X509Certificate

object RSACipher {
  		val cipher_def = "RSA/ECB/PKCS1PADDING"
		val byte_step = 117 //Depends on the used key size... TODO handle this better
		
	def initEncryptionCipher(ks: KeyStore, c_alias:String):Cipher={
  		val cert = getCert(ks,c_alias)		
  		val cipher = Cipher.getInstance(cipher_def) 
  		cipher.init(Cipher.ENCRYPT_MODE, cert)
  		cipher
	}
  		
  	def initDecryptionCipher(ks: KeyStore,c_alias:String):Cipher={
  		val cert = getCert(ks,c_alias)
  		val cipher = Cipher.getInstance(cipher_def) 
  		cipher.init(Cipher.DECRYPT_MODE, cert)
  		cipher
  	}
  	
  	private def getCert(ks: KeyStore, c_alias: String):X509Certificate = {
		//Security.addProvider(new BouncyCastleProvider()) 
		/* Use existing keystore */
  		ks.getCertificate(c_alias) match {
			case c:X509Certificate => c
			case _ => throw new Exception("Woops, not a X509Certificate loaded!")
		}
  	}
}