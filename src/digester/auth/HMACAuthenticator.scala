package digester.hash

import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.Digest
import org.bouncycastle.crypto.params.KeyParameter
import util.HashState
import util.Stringifier

class HMACAuthenticator(keys: Iterator[(String,Array[Byte])], digest: Digest) extends Authenticator{
	
	/**
	 * Using Bouncycastle's Hmac implementation
	 */
  
	val hmac = new HMac(digest)
  
	def authenticate(data: Array[Byte]): HashState = {
	  if (!keys.hasNext) throw new Exception("No more authentication keys!")
	  else {
	    val k = keys.next
		  new HashState(k._1,Stringifier(data),Stringifier(hmac(k._2,data)))
	  }
	}
	
	private def hmac(key: Array[Byte], data: Array[Byte]): Array[Byte] = {
	 val auth = new Array[Byte](hmac.getMacSize()) // TODO check if 'val' is OK here :-)
	 hmac.init(new KeyParameter(key))
	 hmac.update(data,0,data.length)
	 hmac.doFinal(auth,0)
	 auth
	}
}