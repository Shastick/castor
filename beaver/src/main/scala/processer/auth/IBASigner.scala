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
import util.messages.AuthError
import util.messages.IBARefill

/**
 * This class is used to periodically authenticate the state of the hash chain.
 * It uses IBA (Identity Based Authentication) as proposed by Shamir in his 1984 paper
 * "Identity Based CryptoSystems And Signature Schemes"
 */
//TODO split the Authenticator and the verifier in two classes, it's getting too messy.
class IBASigner(var key_reserve: List[(String,RSAPublicKey,Iterator[(String,BigInt)])],
			rangen: Random,
			md: MessageDigest,
			krf: KeyRefiller,
			refill_size: Int) extends Authenticator {
  
  /**
   * TODO make sure it is meaningful => should be of same size than the RSA modulus (?)
   * (And make it coherent with all the key sizes.)
   */
	
	var block_length = 2048
	
	//TODO : make sure this works to define an empty iterator of the correct type...
	var (pkid, pubkey, keys) = ("",
			null.asInstanceOf[RSAPublicKey],
			Iterator.empty.asInstanceOf[Iterator[(String,BigInt)]])
  
	def sign(data: Array[Byte]): HashState = {
	  if(!keys.hasNext) {
	    val tup = popKeys
	    pkid = tup._1
	    pubkey = tup._2
	    keys = tup._3
	    krf ! IBARefill(refill_size)
	  }
	  
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

	    new IBHashState(id,data,s.toByteArray,t.toByteArray,pkid)
	  
	}
	
	def authenticate(h: HashState): AdminMsg = throw new Exception("authenticate() method" +
			"not available in IBA Sign mode.")
	
	/**
	 * Append a new String-Iterator tuple to the key list.
	 */
	def addKeys(kt: (String, RSAPublicKey, Iterator[(String,BigInt)])) = {
	  key_reserve :+= kt
	}
	/**
	 * Removes the head of the key list and returns it.
	 */
	def popKeys(): (String, RSAPublicKey, Iterator[(String, BigInt)]) = {
	  val t = key_reserve.head
	  key_reserve --= List(t)
	  t
	}
	
	
}