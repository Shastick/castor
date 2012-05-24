package util
import org.apache.commons.codec.binary.Base64

class B64(conv: Base64) { 
  def enc(in:Array[Byte]):String = conv.encodeToString(in)
  def dec(in:String):Array[Byte] = conv.decode(Stringifier.toBytes(in))
}

/**
 * Utility for transforming Byte Arrays to BASE64 Strings and vice-versa
 * (A simple wrapper in this case.)
 */
object BASE64 {
  def getConverter = new B64(new Base64)
}