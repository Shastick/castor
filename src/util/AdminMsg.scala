package util

/**
 * Abstract class representing any "meta-message" (or non-content) that would have to be sent around
 * between the actor or written to the output.
 */
abstract class AdminMsg

/**
 * case class wrapper around an encrypted hash.
 */
class HashState(id: String, ctext: String) extends AdminMsg {
  override def toString = id + ":" + ctext
}