package util.messages
import util.BASE64

/**
 * Abstract class representing any "meta-message" (or non-content) that would have to be sent around
 * between the actor or written to the output.
 */
abstract class AdminMsg extends Message {
  /**
   * Setting the default toString to return an empty string for cases where
   * a message should not be written to the output
   * TODO : make sure it is not a bad idea...
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
  /**
   * Constructor variant with byte arrays.
   */
  def this(id: String, hash: Array[Byte], sig: Array[Byte]) =
		  	this(id,BASE64.enc(hash),BASE64.enc(sig))
  
  override def toString = "===== HASH CHAIN STATE FOLLOWS - HMAC MODE : =====\n" + id + ":" + hash + ":" + sig
}

/**
 * Wrapper around a hash, its Identity Based authentication and the ID used to authenticate it.
 *  - id: the of which the private key was used to sign the hash
 * 	- hash: the actual value of the hash chain
 * 	- s,t: the signature of the hash chain
 */
case class IBHashState(id: String, hash: String, s: String, t: String) extends HashState {
  /**
   * Constructor variant with byte arrays.
   */
  def this(id: String, hash: Array[Byte], s: Array[Byte], t: Array[Byte]) =
		  	this(id,BASE64.enc(hash),BASE64.enc(s), BASE64.enc(t))
  
  override def toString = "===== HASH CHAIN STATE FOLLOWS - IBA MODE : =====\n" + id + ":" + hash + ":" + s + ":" + t
}

/**
 * Message used to tell a Hasher he should now write his state to his output.
 */
case object SaveState extends AdminMsg

case class Header(c: String) extends AdminMsg {
  override def toString = "===== " + c + " ====="
}

case class Notification(n: String) extends AdminMsg {
  override def toString = "===== NOTIFICATION: " + n + " ====="
}

case class HashError(e: String) extends AdminMsg {
  override def toString = "===== HASH-ERROR: " + e + " ====="
}

case class ValidHash(id: String) extends AdminMsg {
  override def toString = "Valid hash segment: " + id
}