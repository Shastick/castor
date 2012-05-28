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
import util.messages.AuthRequest
import util.messages.SigRequest
import util.messages.Message

/**
 * A Hasher is a stateful class that will be in charge of building a hash chain of the messages 
 * that flow through it. It will periodically insert the (eventually authentified) current 
 * state of the hash chain into the message flow.
 * 
 */

class Hasher(next: Handler,
    md: MessageDigest,
    auth: Authenticator)
    extends Processer(next) {

  private var lastHash = Array.empty[Byte]
  
  /**
   * customized procAdminMsg for a Hasher.
   * If a save state is received, a message is sent to the authenticator and
   * an answer is awaited (the !? function).
   * This answer is then sent further to the next handler.
   * idem for HashState verification
   */

  override def procAdminMsg(m: AdminMsg) = m match {
    case SaveState => next ! (auth !? SigRequest(lastHash))
    case h: HashState => next ! checkState(h)  
    case _ => next ! m
  } 
  
  /**
   * Handles the hashing
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
   * Compare received versus computed, authenticate if equal
   * or update lastHash with retrieved one if not corresponding.
   */
  private def checkState(h: HashState) = {
    val hb = conv.dec(h.hash)
      if(hb.toSeq == lastHash.toSeq){
        auth !? AuthRequest(h)
      }
      else {
        val err = HashError(h.id, h.hash, conv.enc(lastHash))
		lastHash = hb
		err
      }
  }
}