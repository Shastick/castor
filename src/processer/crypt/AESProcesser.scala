package processer.crypt
import util.cipher.AESCipher
import java.security.Key
import processer.Processer
import processer.Handler

class AESProcesser(next: Handler, k: Key , mode: Int) 
extends Processer(next) {
	
  val cipher = AESCipher.init(k,mode)
  def crunchArray(input: Array[Byte]): Array[Byte] = cipher.crunchArray(input)

}