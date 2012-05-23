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
   * TODO : make the pad_bytes size dynamic (if the key size changes it should adapt)
   */
  
  val default_hash = "SHA-512"
  val digest = MessageDigest.getInstance(default_hash, provider)
  
  val padding_size = 192
  val hash_byte_size = 64
  /**
   * Generates a list of <quant> keys and their associated ID's
   * 
   * The values from 1 to <quant> are hashed and the hash output serves as the Identifier
   * to be encrypted.
   * 
   */
  def genKeys(privKey: RSAPrivateKey, quant: Int): List[(String,BigInt)] = 
  	(1 to quant) map {i => Stringifier(i.toString)} map {i => makeKey(i,privKey)} toList 
 
  	
  def paddID(id: Array[Byte]): Array[Byte] = {
  	    val pad = Array.fill[Byte](padding_size)((new java.lang.Integer(-1)).toByte) _
  	    id ++ pad
  }
  
  /**
   * Build a one-time key.
   */
  private def makeKey(id: Array[Byte], k: RSAPrivateKey): (String, BigInt) = {
  	digest.reset
  	val id_hash = digest.digest(id)
  	val padded = paddID(id_hash)

  	val pk = BigInt(padded).modPow(
  	    new BigInt(k.getPrivateExponent),
  	    new BigInt(k.getModulus))
  	    
  	(BASE64.enc(id_hash),pk)
  }
}