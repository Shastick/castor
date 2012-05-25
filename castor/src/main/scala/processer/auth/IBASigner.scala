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
import util.Keychain
import util.IBAKeychain

/**
 * This class is used to periodically authenticate the state of the hash chain.
 * It uses IBA (Identity Based Authentication) as proposed by Shamir in his 1984 paper
 * "Identity Based CryptoSystems And Signature Schemes"
 */
class IBASigner(var key_reserve: List[IBAKeychain],
			rangen: Random,
			md: MessageDigest,
			krf: KeyRefiller,
			refill_size: Int) extends Authenticator {
  
	//TODO : make coherent with key-size definitions
	var block_length = 2048
	
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

	    new IBHashState(id,conv.enc(data),conv.enc(s.toByteArray),conv.enc(t.toByteArray),pkid)
	  
	}
	
	def authenticate(h: HashState): AdminMsg = throw new Exception("authenticate() method" +
			"not available in IBA Sign mode.")
	
	/**
	 * Append a new String-Iterator tuple to the key list.
	 */
	def addKeys(kc: Keychain) = kc match {
	  case m: IBAKeychain => key_reserve :+= m
	  case _ => throw new Exception("Not a IBAKeychain received for refill.")
	}
	/**
	 * Removes the head of the key list and returns it.
	 */
	def popKeys(): (String, RSAPublicKey, Iterator[(String, BigInt)]) = {
	  val t = key_reserve.head
	  // key_reserve --= List(t) <= this is deprecated.
	  // Compiler asks for the following instead:
	  key_reserve filterNot (List(t) contains)
	  (t.id, t.pub, t.keys)
	}
	
	
}