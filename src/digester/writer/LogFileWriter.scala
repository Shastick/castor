package digester.writer
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import java.io.FileWriter

class LogFileWriter(out: FileWriter, enc: BASE64Encoder) extends LineWriter {

  /**
   * Overloaded constructor for a filename specified by string.
   */
  def this(fname: String, enc: BASE64Encoder) = this(new FileWriter(fname,true),enc)
  
  def writeLine
 
   def base64ToByte(data: String):Array[Byte] = {
       val decoder = new BASE64Decoder()
       decoder.decodeBuffer(data)
   }
 
   def byteToBase64(data: Array[Byte]):String = enc.encode(data)
   
}