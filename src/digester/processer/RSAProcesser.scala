package digester.processer
import java.security.KeyStore

import util.RSACipher


class RSAProcesser(ks: KeyStore) extends LogProcesser(ks) {
	
	val cipher = RSACipher.initEncryptionCipher(ks,"trolilol")
	
	def crunchArray(bytes: Array[Byte]):Array[Byte]={
	  val out = new Array[Byte](bytes.size)
	  val clear_data = bytes.grouped(RSACipher.byte_step)
	  val cipher_data = clear_data.flatMap(b => cipher.doFinal(b))
	  cipher_data.copyToArray(out)
	  out
	}
}