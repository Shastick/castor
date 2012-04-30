package processer.auth
import util.messages.IBHashState
import util.messages.AdminMsg
import util.messages.HashError
import java.security.MessageDigest
import util.BASE64
import util.messages.ValidHash
import util.messages.HashState
import java.util.Random
import java.security.interfaces.RSAPublicKey

/**
 * This class is used to periodically authenticate the state of the hash chain.
 * It uses IBA (Identity Based Authentication) as proposed by Shamir in his 1984 paper
 * "Identity Based CryptoSystems And Signature Schemes"
 */

class IBAuthenticator(keys: Iterator[(String,BigInt)],
			rangen: Random,
			pubkey: RSAPublicKey,
			md: MessageDigest) extends Authenticator {
	
  /**
   * TODO make sure it is meaningful => should be of same size than the RSA modulus (?)
   * (And make it coherent with all the key sizes.)
   */
  
	def block_length = 2048
  
	def sign(data: Array[Byte]): HashState = {
	  //TODO handle this more elegantly than by blowing everything up ;-)
	  if(!keys.hasNext) throw new Exception("No more authentication keys!")
	  else {
	    val e = new BigInt(pubkey.getPublicExponent)
	    val n = new BigInt(pubkey.getModulus)
	    
	    val (id,k) = keys.next
	    
	    val rnd = new Array[Byte](block_length)
	    rangen.nextBytes(rnd)

	    val r = BigInt(rnd)
	    val t = r.modPow(e, n)
	    
	    md.reset
	    val f = BigInt(md.digest(t.toByteArray ++ data))
	    
	    val s = (k*r.modPow(f, n)) mod n

	    new IBHashState(id,data,s.toByteArray,t.toByteArray)
	  }
	}
	
	def authenticate(h: HashState): AdminMsg = h match {
	  case m: IBHashState => verify(m)
	  case _ => HashError("Bad Hash Type received: IBHashSate required, " + h.getClass() + " received instead!")
	}
	
	/**
	 * Check if the received HashState corresponds to the internal state.
	 */
	private def verify(hs: IBHashState): AdminMsg = {
	  
	  val (id,m,s,t) = (BigInt(BASE64.dec(hs.id)),
			  			BASE64.dec(hs.hash),
			  			BigInt(BASE64.dec(hs.s)),
			  			BASE64.dec(hs.t))
	  
	  val (e,n) = (new BigInt(pubkey.getPublicExponent()),
			  		new BigInt(pubkey.getModulus()))
		
	  md.reset
	  val f = BigInt(md.digest(t ++ m))
	  
	  val checkMe = s.modPow(e,n)
	  val shouldBe = (id * BigInt(t).modPow(f,n)) mod n
	  
	  if(checkMe == shouldBe) ValidHash(id.toString)
	  else HashError("Hash segment could not be authenticated, id: " + id.toString)
	}
}