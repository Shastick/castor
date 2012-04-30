package decoder.reader
import java.io.FileReader
import scala.io.Source
import java.io.File
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import util.BASE64
import digester.LogHandler
import util.messages.Parser

/**
 * Implements a simple Iterator returning entire lines, initialized with a file name.
 */
class LogFileInput(dest: LogHandler, lines: Iterator[String]) extends LogInput(dest){
  
  /**
   * Overloaded constructor to build the reader from a file name.
   */
  def this(dest: LogHandler, fname: String) =
    this(dest, Source.fromFile(new File(fname)).getLines)

  def beamLogData = lines.foreach { dest ! Parser.fromLog(_) }
 
}