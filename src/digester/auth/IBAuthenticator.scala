package digester.hash
import uk.ac.ic.doc.jpair.ibe.key.BFMasterPublicKey
import uk.ac.ic.doc.jpair.ibe.key.BFUserPublicKey
import uk.ac.ic.doc.jpair.ibe.BFCipher
import java.util.Random
import uk.ac.ic.doc.jpair.ibe.BFCtext
import util.HashState
import java.security.interfaces.RSAPublicKey
import java.security.MessageDigest
import util.IBHashState

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
   */
	def block_length = 1024
  
	def authenticate(data: Array[Byte]): HashState = {
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
}