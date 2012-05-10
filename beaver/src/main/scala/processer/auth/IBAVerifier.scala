package processer.auth
import util.messages.IBHashState
import util.messages.AdminMsg
import util.messages.HashError
import util.BASE64
import util.messages.ValidHash
import util.messages.AuthError
import util.messages.HashState
import util.ManagedKeyStore
import java.security.MessageDigest
import java.security.interfaces.RSAPublicKey

class IBAVerifier(ks: ManagedKeyStore, md: MessageDigest) extends Authenticator {
	var block_length = 2048
	
  	def authenticate(h: HashState): AdminMsg = h match {
	  case m: IBHashState => verify(m)
	  case _ => HashError("Bad Hash Type received: IBHashSate required, " + h.getClass() + " received instead!")
	}
  	
  	/**
	 * Check if the received HashState corresponds to the internal state.
	 */
	private def verify(hs: IBHashState): AdminMsg = {
	  val (id_in,m,s,t,kid) = (BASE64.dec(hs.id),
			  			BASE64.dec(hs.hash),
			  			BigInt(BASE64.dec(hs.s)),
			  			BASE64.dec(hs.t),
			  			hs.kid)
	  
      val pubkey = ks.readPublicKey(kid)
			  			//TODO DO THE PADDING IN ONE PLACE
      val pad_bytes = 192
	  val pad = Array.fill[Byte](pad_bytes)((new java.lang.Integer(-1)).toByte) _

  	  val id = BigInt(id_in ++ pad)
	  val (e,n) = (new BigInt(pubkey.getPublicExponent()),
			  		new BigInt(pubkey.getModulus()))
		
	  md.reset
	  val f = BigInt(md.digest(t ++ m))
	  
	  val checkMe = s.modPow(e,n)
	  val shouldBe = (id * BigInt(t).modPow(f,n)) mod n
	  
	  if(checkMe == shouldBe) ValidHash(hs.hash)
	  else AuthError("Segment could not be authenticated, id: " + hs.id)
	}
	
	def addKeys(t: (String, RSAPublicKey, Iterator[(String, BigInt)])) = throw new Exception("addKeys() not available in verification mode.")
	def sign(d: Array[Byte]) = throw new Exception("sign() not available in verification mode.")
}