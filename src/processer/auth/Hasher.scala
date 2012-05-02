package processer.auth
import util.messages.AdminMsg
import java.security.MessageDigest
import util.messages.SaveState
import util.messages.HashState
import util.messages.SyslogMsg
import processer.Processer
import processer.Handler
import util.Stringifier
import util.messages.HashError
import util.BASE64
import java.util.Random
import util.hasher.IBAKeyGen
import java.security.interfaces.RSAPublicKey
import java.security.interfaces.RSAPrivateKey

/**
 * A Hasher is a stateful class that will be in charge of building a hash chain of the messages 
 * that flow through it. It will periodically insert the (eventually authentified) current 
 * state of the hash chain into the message flow.
 * 
 */

class Hasher(next: Handler,
    md: MessageDigest,
    sc: Authenticator)
    extends Processer(next) {

  private var lastHash = Array.empty[Byte]
  
  /**
   * customized procAdminMsg for a Hasher 
   */
  override def procAdminMsg(m: AdminMsg) = m match {
    case SaveState => next ! sc.sign(lastHash)
    case h: HashState =>  next ! verify(h)
    case _ => next ! m
  } 
  
  /**
   * Handles the hashing TODO : is a salt required here ?
   */
  def crunchArray(in: Array[Byte]): Array[Byte] = {
    md.reset
    md.digest(in)
  }
  /**
   * The message is not altered and returned as is.
   * The hash is computed from the whole message converted to bytes.
   */
  override def crunchDgram(in: SyslogMsg): SyslogMsg = {
    val toHash = lastHash ++ in.toBytes
    lastHash = crunchArray(toHash)
    in
  }
  
  /**
   * Compare received versus computed hash and authenticate if equal
   */
  
  def verify(h: HashState): AdminMsg =
    if(BASE64.dec(h.hash).toSeq == lastHash.toSeq) sc.authenticate(h)
    else HashError("Hash states not corresponding for block ID: " + h.id)

}

/**
 * Companion Object
 */
object Hasher {
  val digest = MessageDigest.getInstance("SHA-512")
  val rnd = new Random
  
  /**
   * Generate a new identity based hasher, using a master private key to generate a one-time
   * authentication secrets list, and verkey for verification.
   */
  def makeFullIBHasher(next: Handler, masterPKey: RSAPrivateKey, verkey: RSAPublicKey, qtt: Int): Hasher = {
    val kl = IBAKeyGen.genKeys(masterPKey,qtt)
    val auth = new IBAuthenticator(kl.toIterator,rnd,verkey,digest)
    new Hasher(next, digest, auth)
  }
  
  /**
   * This hasher is for verification only (if used for authentication it will throw an exception)
   */
  def makeEmptyIBHasher(next: Handler, verkey: RSAPublicKey): Hasher = {
    val auth = new IBAuthenticator(Iterator.empty, null, verkey, digest)
    new Hasher(next, digest, auth)
  }
}