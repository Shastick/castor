package util
import javax.crypto.Cipher
import java.security.KeyStore
import java.security.cert.X509Certificate

object RSACipher {
  		val cipher_def = "RSA/ECB/PKCS1PADDING"
		val byte_step = 117
	def initCipher(ks: KeyStore, c_alias:String):Cipher={
		/**
		 * Courtesy of http://www.jensign.com/JavaScience/dotnet/RSAEncrypt/
		 */
	
		//Security.addProvider(new BouncyCastleProvider()) 
		/* Use existing keystore */ 
		val cert = ks.getCertificate(c_alias) match {
			case c:X509Certificate => c
			case _ => throw new Exception("Woops, not a X509Certificate loaded!")
		}
	
	val cipher = Cipher.getInstance(cipher_def) 
	cipher.init(Cipher.ENCRYPT_MODE, cert)
	cipher
	}
}