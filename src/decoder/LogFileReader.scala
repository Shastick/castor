package decoder
import java.io.FileReader
import scala.io.Source
import java.io.File

/**
 * Implements a simple Iterator returning entire lines, initialized with a file name.
 */
class LogFileReader(lines: Iterator[String]) extends LogReader{
	
  def this(fname: String) = this(Source.fromFile(new File(fname)).getLines)

  def hasNext() = lines.hasNext
  def next() = lines.next()
}