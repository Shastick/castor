package digester.processer
import java.security.KeyStore

import util.RSACipher


class RSAProcesser(ks: KeyStore, ka: String) extends LogProcesser(ks,ka) {
	
	val cipher = RSACipher.initEncryptionCipher(ks,ka)
	
	def crunchArray(bytes: Array[Byte]):Array[Byte]={
	  val out = new Array[Byte](cipher.getOutputSize(bytes.size))
	  val clear_data = bytes.grouped(cipher.getBlockSize())
	  val cipher_data = clear_data.flatMap(b => cipher.doFinal(b))
	  cipher_data.copyToArray(out)
	  out
	}
}