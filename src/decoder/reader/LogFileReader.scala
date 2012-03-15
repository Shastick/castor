package decoder.reader
import java.io.FileReader
import scala.io.Source
import java.io.File
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder

/**
 * Implements a simple Iterator returning entire lines, initialized with a file name.
 */
class LogFileReader(lines: Iterator[String],dec: BASE64Decoder) extends LogReader{
	
  def this(fname: String) = this(Source.fromFile(new File(fname)).getLines,new BASE64Decoder)

  def hasNext() = lines.hasNext
  def next() = base64ToByte(lines.next())
  
  def base64ToByte(data: String):Array[Byte] = dec.decodeBuffer(data)
}