package util.hasher
import java.security.MessageDigest
import java.security.interfaces.RSAPrivateKey
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider
import javax.crypto.Cipher
import java.nio.ByteBuffer
import util.Stringifier
import java.security.interfaces.RSAPublicKey
import java.security.KeyPairGenerator
import util.BASE64

/**
 * Object intended for disposable keys list and associated master keys generation.
 * 
 * Disposable keys are generated according to Shamir's proposal of IBA.
 */
object IBAKeyGen {
  
  val int_size = 8

  val provider = "BC"
  Security.addProvider(new BouncyCastleProvider())
  
  /**
   * Default Hash we will use
   * TODO : also use BouncyCastle here for coherence ?
   */
  
  val default_hash = "SHA-512"
  val digest = MessageDigest.getInstance(default_hash, provider)
    
  /**
   * Generates a list of <quant> keys and their associated ID's
   * 
   * The values from 1 to <quant> are hashed and the hash output serves as the Identifier
   * to be encrypted.
   * 
   * TODO : NO PADDING IS CURRENTLY USED. CHECK IF THIS MUST BE CORRECTED
   * 	=> is this really a problem, as the 'encrypted' message H(id) is known ?
   * 	=> a standard padding scheme cannot be used as the private key is never 'decrypted'
   * 	=> hence, the padding would be predictive and would not add much ?
   * 
   */
  def genKeys(privKey: RSAPrivateKey, quant: Int): List[(String,BigInt)] = {
  	
  	(1 to quant) map {i => Stringifier(i.toString)} map {i => makeKey(i,privKey)} toList 
  }
  
  /**
   * Generate a new random key pair.
   */
  def genKeyPair(bit_size: Int): (RSAPublicKey,RSAPrivateKey) = {
    val kg = KeyPairGenerator.getInstance("RSA")
    kg.initialize(bit_size)
    val kp = kg.genKeyPair()
    (kp.getPublic.asInstanceOf[RSAPublicKey],kp.getPrivate.asInstanceOf[RSAPrivateKey])
  }
  
  /**
   * Build a key.
   * TODO : check it does the RSA stuff as I think it does, or verification will fail.
   */
  private def makeKey(id: Array[Byte], k: RSAPrivateKey): (String, BigInt) = {
  	digest.reset
  	
  	val id_hash = digest.digest(id)
  	val pk = BigInt(id_hash).modPow(
  	    new BigInt(k.getPrivateExponent),
  	    new BigInt(k.getModulus))
  	    
  	(BASE64.enc(id_hash),pk)
  }
}