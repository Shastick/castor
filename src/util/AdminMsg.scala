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
  override def toString = id + ":" + hash + ":" + sig
}