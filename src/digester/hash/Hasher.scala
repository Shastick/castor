package digester.hash
import digester.LogProcesser
import util.SyslogMsg
import digester.LogHandler
import java.security.MessageDigest

/**
 * A Hasher is a statefull class that will be in charge of building a hash chain of the messages 
 * that flow through it. It will periodically insert the (encrypted) current 
 * state of the hash chain into the message flow.
 * 
 */
class Hasher(next: LogHandler, md: MessageDigest) extends LogProcesser(next) {
  
  def crunchArray(in: Array[Byte]): Array[Byte] = {
    Array.empty[Byte]
  }
}

/**
 * Helper class
 */
object Hasher {
  
}