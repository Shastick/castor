package util.hasher
import java.security.MessageDigest
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider
import util.HMACKeychain
import util.Stringifier

/**
 * Object intended for disposable keys list generation.
 * 
 * Disposable keys are generated by providing a secret as a seed,
 * it is intended to be a 'lazy' IBA replacement.
 */

object HMACKeyGen {
	
    val provider = "BC"
    Security.addProvider(new BouncyCastleProvider())
  
	val default_hash = "SHA-512"
    val digest = MessageDigest.getInstance(default_hash, provider)
    
    /**
	 * Generate qtt secret keys based on the provided secret.
	 * keys are obtained by successively hashing the secret and using the secret as 
	 * a hash at each round.
	 */
    
    def genKeys(secret: String, qtt: Int):HMACKeychain = genKeys(Stringifier(secret), qtt)
    
	def genKeys(secret: Array[Byte], qtt: Int): HMACKeychain = {
	  var keys = List[(String,Array[Byte])]()
	  var prev_hash = secret
	  
	  for(i <- 1 to qtt){
	    val id = i.toString
	    val k = digest.digest(secret ++ prev_hash)
	    keys :+= (id,k)
	  }
	  
	  HMACKeychain(keys.toIterator)
	}
}