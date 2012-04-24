package digester.hash
import digester.LogProcesser
import util.SyslogMsg
import digester.LogHandler
import java.security.MessageDigest
import scala.actors.Actor

/**
 * A Hasher is a statefull class that will be in charge of building a hash chain of the messages 
 * that flow through it. It will periodically insert the (encrypted) current 
 * state of the hash chain into the message flow.
 * 
 */
class Hasher(next: LogHandler, md: MessageDigest) extends LogProcesser(next) {

  private var lastHash = Array.empty[Byte]
  
  /**
   * Handles the hashing TODO : is a salt required here ?
   */
  def crunchArray(in: Array[Byte]): Array[Byte] = {
    md.reset
    md.digest(in)
  }
  
  override def crunchDgram(in: SyslogMsg): SyslogMsg = {
    val toHash = lastHash ++ in.toBytes
    lastHash = crunchArray(toHash)
    in
  }
}

/**
 * Helper class
 */
object Hasher {
  
}