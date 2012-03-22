package util

import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder

/**
 * Utility singleton for transforming Byte Arrays to BASE64 Strings and vice-versa
 */
object BASE64 {
  val enc = new BASE64Encoder
  val dec = new BASE64Decoder
  
  def enc(in:Array[Byte]):String = enc.encode(in) 
  def dec(in:String):Array[Byte] = dec.decodeBuffer(in)
  
  def getEncoder = new BASE64Encoder
  def getDecoder = new BASE64Decoder
}