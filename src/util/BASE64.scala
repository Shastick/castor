package util

import org.apache.commons.codec.binary.Base64

/**
 * Utility singleton for transforming Byte Arrays to BASE64 Strings and vice-versa
 */
object BASE64 {
  val conv = new Base64()
  
  
  def enc(in:Array[Byte]):String = conv.encodeToString(in)
  def dec(in:String):Array[Byte] = conv.decode(Stringifier.toBytes(in))
  
  def getConverter = new Base64
}