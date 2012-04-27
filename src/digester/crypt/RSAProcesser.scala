package digester.crypt
import java.security.KeyStore
import util.cipher.RSACipher
import util.messages.SyslogMsg
import digester.LogHandler
import digester.LogProcesser
import javax.crypto.Cipher
import java.security.Key


class RSAProcesser(next: LogHandler, k: Key , mode: Int) extends LogProcesser(next) {
	
	val cipher = RSACipher.init(k,mode) 
	
	def crunchArray(bytes: Array[Byte]): Array[Byte] = cipher.crunchArray(bytes)
	
}