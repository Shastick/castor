package util.hasher
import java.security.MessageDigest
import java.security.interfaces.RSAPrivateKey
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider
import javax.crypto.Cipher
import java.nio.ByteBuffer

/**
 * Object intended for disposable keys list and associated master keys generation.
 * 
 * Disposable keys are generated according to Shamir's proposal of IBA.
 */
object IBAKeyGen {
  
  val int_size = 8
  
  /**
   * We explicitely use no padding . It will be 
   */
  
  val cipher_def = "RSA/ECB/NOPADDING"
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
   * 
   */
  def genKeys(privKey: RSAPrivateKey, quant: Int): List[(String,BigInt)] = {
    
    val cipher = Cipher.getInstance(cipher_def,provider) 
  		cipher.init(Cipher.ENCRYPT_MODE, privKey)
  	
  		
  	val keys = List.empty[(String,BigInt)]
  	for(i <- 1 to quant) {
  	  var i_byte = new Array[Byte](int_size)
  	  bb.putInt(i)
  	  bb.flip
  	  bb.get(i_byte)
  	  
  	  digest.reset
  	  val id = digest.digest(i_byte)
  	  
  	  
  	}
  	keys
  }
  
  /**
   * Build a key.
   * TODO : check it does the RSA stuff as I think it does, or verification will fail.
   */
  private def makeKey(seqNum: Int, c: Cipher): (String, BigInt) = {
  	  digest.reset
  	  val bytes = IntToBytes(seqNum)
  	  val id = digest.digest(bytes)
  	  val pk = c.doFinal(id)
  	  
  }
  
  private def IntToBytes(i: Int): Array[Byte] = {
    val bb = ByteBuffer.allocate(int_size)
    var i_byte = new Array[Byte](int_size)
  	bb.putInt(seqNum)
  	bb.flip
  	bb.get(i_byte)
  	bb.clear  
  	i_byte
  }
}