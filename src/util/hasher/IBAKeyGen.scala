package util.hasher
import java.security.MessageDigest
import java.security.interfaces.RSAPrivateKey
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider

/**
 * Object intended for disposable keys list and associated master keys generation.
 * 
 * Disposable keys are generated according to Shamir's proposal of IBA.
 */
object IBAKeyGen {
  
  /**
   * We explicitely use no padding as we will be trunkating the hashe's output to have the exact 
   * length. 
   */
  
  val cipher_def = "RSA/ECB/NOPADDING"
  val provider = "BC"
  Security.addProvider(new BouncyCastleProvider())
  
  /**
   * Generates a list of <quant> keys and their associated ID's
   * 
   * The values from 1 to <quant> are hashed and the hash output serves as the Identifier
   * to be encrypted.
   */
  def genKeys(privKey: RSAPrivateKey, md: MessageDigest, quant: Int): List[(String,BigInt)] = {
    
  }
}