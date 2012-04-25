package digester.hash
import java.security.MessageDigest

import digester.LogHandler
import digester.LogProcesser
import util.HashState
import util.Stringifier
import util.SyslogMsg

/**
 * A Hasher is a statefull class that will be in charge of building a hash chain of the messages 
 * that flow through it. It will periodically insert the (eventually authentified) current 
 * state of the hash chain into the message flow.
 * 
 */

class Hasher(next: LogHandler,
    md: MessageDigest,
    sc: Authenticator)
    extends LogProcesser(next) {

  private var lastHash = Array.empty[Byte]
  
  /**
   * Handles the hashing TODO : is a salt required here ?
   */
  def crunchArray(in: Array[Byte]): Array[Byte] = {
    md.reset
    md.digest(in)
  }
  /**
   * The message is not altered and returned as is.
   */
  override def crunchDgram(in: SyslogMsg): SyslogMsg = {
    val toHash = lastHash ++ in.toBytes
    lastHash = crunchArray(toHash)
    in
  }
  
  /**
   * Authentifies the current state and sends it to the downstream LogHandler.
   */
  def writeState() =
    next ! sc.authenticate(lastHash)
}

/**
 * Companion Object
 */
object Hasher {
  
}