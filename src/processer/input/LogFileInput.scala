package processer.input
import processer.Handler
import scala.io.Source
import java.io.File
import util.messages.Parser

/**
 * Implements a simple Iterator returning entire lines, initialized with a file name.
 */
class LogFileInput(dest: Handler, lines: Iterator[String]) extends LogInput(dest){
  
  /**
   * Overloaded constructor to build the reader from a file name.
   */
  def this(dest: Handler, fname: String) =
    this(dest, Source.fromFile(new File(fname)).getLines)

  def beamLogData = lines.foreach { dest ! Parser.fromLog(_) }
 
}