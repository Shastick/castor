package digester.processer
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import java.security.Security
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.io.FileInputStream


class RSAProcesser extends LogProcesser{
	val cipher_def = "RSA/ECB/PKCS1PADDING"
	val RSA_byte_step = 117
	
	/**
	 * Courtesy of http://www.jensign.com/JavaScience/dotnet/RSAEncrypt/
	 */
	
	//Security.addProvider(new BouncyCastleProvider()) 
	/* Use existing keystore */ 
	val ALIAS = "trolilol"
	// keystore alias 
	val keystore = KeyStore.getInstance("JKS")
	keystore.load(new FileInputStream("keystore"), null)
	val cert = keystore.getCertificate(ALIAS) match {
	  case c:X509Certificate => c
	  case _ => throw new Exception("Woops, not a X509Certificate loaded!")
	}
	
	val cipher = Cipher.getInstance(cipher_def) 
	cipher.init(Cipher.ENCRYPT_MODE, cert)
	
	def crunchLine(bytes: Array[Byte]):Array[Byte]={
	  val out = new Array[Byte](bytes.size)
	  val clear_data = bytes.grouped(RSA_byte_step)
	  val cipher_data = clear_data.flatMap(b => cipher.doFinal(b))
	  cipher_data.copyToArray(out)
	  out
	}
}