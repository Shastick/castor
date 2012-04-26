package util

/**
 * Abstract class representing any "meta-message" (or non-content) that would have to be sent around
 * between the actor or written to the output.
 */
abstract class AdminMsg

/**
 * Wrapper around a hash, its authentication and the ID used to authenticate it.
 */
class HashState(id: String, hash: String, sig: String) extends AdminMsg {
  /**
   * Constructor variant with byte arrays.
   */
  def this(id: String, hash: Array[Byte], sig: Array[Byte]) =
		  	this(id,Stringifier(hash),Stringifier(sig))
  
  override def toString = id + ":" + hash + ":" + sig
}

class IBHashState(id: String, hash: String, s: String, t: String)
	extends HashState(id, hash, s) {
  /**
   * Constructor variant with byte arrays.
   */
  def this(id: String, hash: Array[Byte], s: Array[Byte], t: Array[Byte]) =
		  	this(id,Stringifier(hash),Stringifier(s), Stringifier(t))
  
  override def toString = id + ":" + hash + ":" + s + ":" + t
}