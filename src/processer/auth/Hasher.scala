package processer.auth
import util.messages.AdminMsg
import java.security.MessageDigest
import util.messages.SaveState
import util.messages.HashState
import util.messages.SyslogMsg
import processer.Processer
import processer.Handler

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
   * 
   */
  override def procAdminMsg(m: AdminMsg) = m match {
    case SaveState => next ! sc.sign(lastHash)
    case h: HashState =>  next ! sc.authenticate(h)
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
}

/**
 * Companion Object
 */
object Hasher {
  
}