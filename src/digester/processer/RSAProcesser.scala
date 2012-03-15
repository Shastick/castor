package digester.processer
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import java.security.Security
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.io.FileInputStream
import util.RSACipher


class RSAProcesser(ks: KeyStore) extends ManagedKey(ks) with LogProcesser{
	
	val cipher = RSACipher.initEncryptionCipher(ks,"trolilol")
	
	def crunchLine(bytes: Array[Byte]):Array[Byte]={
	  val out = new Array[Byte](bytes.size)
	  val clear_data = bytes.grouped(RSACipher.byte_step)
	  val cipher_data = clear_data.flatMap(b => cipher.doFinal(b))
	  cipher_data.copyToArray(out)
	  out
	}
}