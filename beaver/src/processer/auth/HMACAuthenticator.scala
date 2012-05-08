package processer.auth
import util.messages.AdminMsg
import util.messages.HashError
import org.bouncycastle.crypto.macs.HMac
import util.messages.HMACState
import util.messages.HashState
import org.bouncycastle.crypto.Digest
import util.Stringifier
import org.bouncycastle.crypto.params.KeyParameter

class HMACAuthenticator(keys: Iterator[(String,Array[Byte])], digest: Digest) extends Authenticator{
	
	/**
	 * Using Bouncycastle's Hmac implementation
	 */
  
	val hmac = new HMac(digest)
  
	def sign(data: Array[Byte]): HMACState = {
	  if (!keys.hasNext) throw new Exception("No more authentication keys!")
	  else {
	    val k = keys.next
		  new HMACState(k._1,Stringifier(data),Stringifier(hmac(k._2,data)))
	  }
	}
	
	def authenticate(h: HashState): AdminMsg = HashError("")
	
	private def hmac(key: Array[Byte], data: Array[Byte]): Array[Byte] = {
	 val auth = new Array[Byte](hmac.getMacSize()) // TODO check if 'val' is OK here :-)
	 hmac.init(new KeyParameter(key))
	 hmac.update(data,0,data.length)
	 hmac.doFinal(auth,0)
	 auth
	}
}