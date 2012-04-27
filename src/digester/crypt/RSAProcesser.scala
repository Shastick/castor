package digester.crypt
import java.security.KeyStore
import util.cipher.RSACipher
import util.messages.SyslogMsg
import digester.LogHandler
import digester.LogProcesser


class RSAProcesser(next: LogHandler, ks: KeyStore, ka: String) extends LogProcesser(next) {
	
	val cipher = RSACipher.initEncryptionCipher(ks,ka)
	def crunchArray(bytes: Array[Byte]): Array[Byte] = cipher.crunchArray(bytes)
	
}