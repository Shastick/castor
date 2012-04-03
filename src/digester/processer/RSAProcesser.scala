package digester.processer
import java.security.KeyStore

import util.cipher.RSACipher


class RSAProcesser(ks: KeyStore, ka: String) extends LogProcesser(ks,ka) {
	
	val cipher = RSACipher.initEncryptionCipher(ks,ka)
	
	def crunchArray(bytes: Array[Byte]):Array[Byte]= cipher.crunchArray(bytes)
}