package decoder
import java.io.FileReader
import scala.io.Source
import java.io.File

class LogFileReader(lines: Iterator[String]) extends Iterator[String]{
	
  def this(fname: String) = this(Source.fromFile(new File(fname)).getLines)

  def readLine():Option[String] = {
	if(lines.hasNext) Some(lines.next())
		else None
  }
}