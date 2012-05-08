package processer.crypt
import util.cipher.RSACipher
import java.security.Key
import processer.Handler
import processer.Processer








class RSAProcesser(next: Handler, k: Key , mode: Int) extends Processer(next) {
	
	val cipher = RSACipher.init(k,mode) 
	
	def crunchArray(bytes: Array[Byte]): Array[Byte] = cipher.crunchArray(bytes)
	
}