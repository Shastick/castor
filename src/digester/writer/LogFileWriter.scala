package digester.writer
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import java.io.FileWriter

class LogFileWriter(out: FileWriter, enc: BASE64Encoder, line_sep: String) extends LineWriter {

  /**
   * Overloaded constructor for a filename specified by string.
   */
  def this(fname: String) = this(new FileWriter(fname,true), new BASE64Encoder, "\n")
  
  def writeLine(line: String) = {
    if (line.endsWith(line_sep)) out.write(line)
    	else out.write(line + line_sep)
    out.flush()
  }
  
  def writeLine(bytes: Array[Byte]) = writeLine(byteToBase64(bytes))
 
  def byteToBase64(data: Array[Byte]):String = enc.encode(data)
   
}