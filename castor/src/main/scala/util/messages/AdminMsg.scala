package util.messages
import util.BASE64
import java.security.interfaces.RSAPublicKey
import util.IBAKeychain

/**
 * Abstract class representing any "meta-message" (or non-content) that would have to be sent around
 * between the actor or written to the output.
 */
abstract class AdminMsg extends Message {
  /**
   * Setting the default toString to return an empty string for cases where
   * a message should not be written to the output
   */
  override def toString: String = ""
}

abstract class HashState extends AdminMsg {
  def id: String
  def hash: String
}

/**
 * Wrapper around a hash, its authentication and the ID used to authenticate it.
 */
case class HMACState(id: String, hash: String, sig: String) extends HashState {
  override def toString = "===== HASH CHAIN STATE - HMAC MODE : =====\n" + id + ":" + hash + ":" + sig
}

/**
 * Wrapper around a hash, its Identity Based authentication and the ID used to authenticate it.
 *  - id: the of which the private key was used to sign the hash
 * 	- hash: the actual value of the hash chain
 * 	- s,t: the signature of the hash chain
 *  - kid: the alias of the public key needed for verification
 */
case class IBHashState(id: String, hash: String, s: String, t: String, kid: String) extends HashState {  
  override def toString = "===== HASH CHAIN STATE - IBA MODE : =====\n" +
		  				id + ":" + hash + ":" + s + ":" + t + ":" + kid
}

/**
 * Message used to tell a Hasher he should now write his state to his output.
 */
case object SaveState extends AdminMsg

/**
 * End of stream
 */
case object EndOfStream extends AdminMsg

case class Header(c: String) extends AdminMsg {
  override def toString = "===== " + c + " ====="
}

case class Notification(n: String) extends AdminMsg {
  override def toString = "===== NOTIFICATION: " + n + " ====="
}

case class MalformedSyslogInput(d: String) extends AdminMsg {
  override def toString = "===== ERROR - MALFORMED INPUT: " + d + " ====="
}

case object EmptyLine extends AdminMsg {
  override def toString = "===== EMPTY INPUT LINE ====="
}

/**
 * Authentication status messages
 */
abstract class Error extends AdminMsg

case class HashError(id: String, expected: String, computed: String) extends Error {
  override def toString = "HASH-ERROR: Hash states not corresponding for segment ID: " + id + 
  		" expected: " + expected + " found: " + computed
}

case class HashTypeError(e: String) extends Error {
  override def toString = "HASH-TYPE ERROR: " + e
}

case class AuthError(id: String, hash: String) extends Error {
  override def toString = "AUTH-ERROR: Segment could not be authenticated, id: " + id + " hash: " + hash
}

case class ValidHash(id: String, h: String) extends AdminMsg {
  override def toString = "Valid segment, id: " + id + " hash: " + h
}
/**
 * Authentication requests traffic
 */

case class SigRequest(a: Array[Byte])
case class AuthRequest(m: HashState)
/**
 * Keylist Refilling messages
 */
case class IBARefill(n: Int)
case class IBAKeys(k: IBAKeychain)