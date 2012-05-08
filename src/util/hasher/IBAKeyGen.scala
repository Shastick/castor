package util.hasher
import java.security.KeyPairGenerator
import java.security.MessageDigest
import util.BASE64
import util.Stringifier
import java.security.Security
import java.security.interfaces.RSAPublicKey
import java.security.interfaces.RSAPrivateKey
import org.bouncycastle.jce.provider.BouncyCastleProvider

/**
 * Object intended for disposable keys list and associated master keys generation.
 * 
 * Disposable keys are generated according to Shamir's proposal of IBA.
 */
object IBAKeyGen {

  val provider = "BC"
  Security.addProvider(new BouncyCastleProvider())
  
  /**
   * Default Hash we will use
   * TODO : also use BouncyCastle here for coherence ?
   */
  
  val default_hash = "SHA-512"
  val digest = MessageDigest.getInstance(default_hash, provider)
  
  val pad_bytes = 192
  val hash_byte_size = 64 
  /**
   * Generates a list of <quant> keys and their associated ID's
   * 
   * The values from 1 to <quant> are hashed and the hash output serves as the Identifier
   * to be encrypted.
   * 
   * TODO : actually padding the end of the id string with all ones.
   * 
   */
  def genKeys(privKey: RSAPrivateKey, quant: Int): List[(String,BigInt)] = 
  	(1 to quant) map {i => Stringifier(i.toString)} map {i => makeKey(i,privKey)} toList 
 
  
  /**
   * Build a one-time key.
   */
  private def makeKey(id: Array[Byte], k: RSAPrivateKey): (String, BigInt) = {
  	digest.reset
  	// Pad the hash with all ones TODO : do it in one place...
  	val pad = Array.fill[Byte](pad_bytes)((new java.lang.Integer(-1)).toByte) _
  	val id_hash = digest.digest(id)
  	val padded = id_hash ++ pad

  	val pk = BigInt(padded).modPow(
  	    new BigInt(k.getPrivateExponent),
  	    new BigInt(k.getModulus))
  	    
  	(BASE64.enc(id_hash),pk)
  }
}