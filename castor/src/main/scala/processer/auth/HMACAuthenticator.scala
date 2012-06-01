package processer.auth
import util.messages.AdminMsg
import util.messages.HashError
import org.bouncycastle.crypto.macs.HMac
import util.messages.HMACState
import util.messages.HashState
import org.bouncycastle.crypto.Digest
import util.Stringifier
import org.bouncycastle.crypto.params.KeyParameter
import util.HMACKeychain
import util.Keychain
import util.BASE64
import util.messages.HashTypeError
import util.messages.ValidHash
import util.messages.AuthError


class HMACAuthenticator(kc: HMACKeychain, digest: Digest) extends Authenticator{
	
	/**
	 * Using Bouncycastle's Hmac implementation
	 */
  
	val hmac = new HMac(digest)
	
	val keys = kc.keys
  
	def sign(data: Array[Byte]): HMACState = 
	  if (!keys.hasNext) throw new Exception("No more authentication keys!")
	  else {
	    val (id,k) = keys.next
		new HMACState(id,conv.enc(data),conv.enc(hmac(k,data)))
	  }
	
	/**
	 * It is crucial that hash states are verified in the exact same order as key lookup
	 * currently isn't implemented.
	 */
	def authenticate(h: HashState): AdminMsg = h match {
	  case h: HMACState => verify(h)
	  case _ => HashTypeError("Bad Hash Type received: IBHashSate required, " +
	      h.getClass() + " received instead!")
	}
	
	private def verify(h: HMACState) = {
	  val (i,k) = keys.next
	  val m = conv.dec(h.hash)
	  val should = hmac(k,m).toSeq
	  val sig = conv.dec(h.sig).toSeq
	  
	  if (should equals sig) ValidHash(h.id, h.hash)
	  else AuthError(h.id, h.hash)
	}
	
	private def hmac(key: Array[Byte], data: Array[Byte]): Array[Byte] = {
	 var auth = new Array[Byte](hmac.getMacSize())
	 hmac.init(new KeyParameter(key))
	 hmac.update(data,0,data.length)
	 hmac.doFinal(auth,0)
	 auth
	}
	
	def addKeys(kc: Keychain) = throw new Exception("HMACAuthenticator currently does not support " +
			"key refilling.")
}