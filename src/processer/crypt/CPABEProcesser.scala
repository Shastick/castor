package processer.crypt
import util.messages.SyslogMsg
import processer.Processer
import processer.Handler

/**
 * This class is a wrapper around a the cpabe utility written
 * by John Bethencourt => http://acsc.cs.utexas.edu/cpabe/
 * It's ugly as it will write and read files to handle the encryption of 
 * a byte array via exec calls and that cpabe can only take input from
 * files.
 * 
 * Constructor parameters :
 *  - pub_key : location of the public key used to encrypt data with cpabe
 *  - tempfile : file where data to be encrypted is written to
 */
class CPABEProcesser(next: Handler, pub_key: String, tempfile: String) extends Processer(next) {
  
  override def crunchDgram(m: SyslogMsg):SyslogMsg = null
  
  def crunchArray(in: Array[Byte]):Array[Byte] = {
  Array.empty  
  }
}