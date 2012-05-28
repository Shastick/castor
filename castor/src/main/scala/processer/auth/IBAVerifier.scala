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
import util.Keychain
import util.hasher.IBAKeyGen
import util.messages.HashTypeError

class IBAVerifier(ks: ManagedKeyStore, md: MessageDigest) extends Authenticator {
	var block_length = 2048
	
  	def authenticate(h: HashState): AdminMsg = h match {
	  case m: IBHashState => verify(m)
	  case _ => HashTypeError("Bad Hash Type received: IBHashSate required, " + h.getClass() + " received instead!")
	}
  
  	/**
	 * Check if the received HashState corresponds to the internal state.
	 */
	private def verify(hs: IBHashState): AdminMsg = {
	  val (id_in,m,s,t,kid) = (conv.dec(hs.id),
			  			conv.dec(hs.hash),
			  			BigInt(conv.dec(hs.s)),
			  			conv.dec(hs.t),
			  			hs.kid)
	  
      val pubkey = ks.readPublicKey(kid)
	  
  	  val id = BigInt(IBAKeyGen.paddID(id_in))
	  val (e,n) = (new BigInt(pubkey.getPublicExponent()),
			  		new BigInt(pubkey.getModulus()))
		
	  md.reset
	  val f = BigInt(md.digest(t ++ m))
	  
	  val checkMe = s.modPow(e,n)
	  val shouldBe = (id * BigInt(t).modPow(f,n)) mod n
	  
	  if(checkMe == shouldBe) ValidHash(hs.id, hs.hash)
	  else AuthError(hs.id, hs.hash)
	}
	
	def addKeys(kc: Keychain) = throw new Exception("addKeys() not available in verification mode.")
	def sign(d: Array[Byte]) = throw new Exception("sign() not available in verification mode.")
}