package decoder.reader
import java.io.FileReader
import scala.io.Source
import java.io.File
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import util.BASE64

/**
 * Implements a simple Iterator returning entire lines, initialized with a file name.
 */
class LogFileReader(lines: Iterator[String]) {
  val dec = BASE64.getDecoder
  
  def this(fname: String) = this(Source.fromFile(new File(fname)).getLines)

  def hasNext = lines.hasNext
  def next = dec.decodeBuffer(lines.next)
 
}