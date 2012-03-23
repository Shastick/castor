package digester.writer
import java.io.FileWriter
import util.SyslogMsg
import util.BASE64

class LogFileWriter(out: FileWriter, line_sep: String) extends LineWriter {
  
  /**
   * Overloaded constructor for a filename specified by string.
   */
  
  def this(fname: String) = this(new FileWriter(fname,true), "\n")
  
  def writeLine(line: String) = {
    if (line.endsWith(line_sep)) out.write(line)
    	else out.write(line + line_sep)
    out.flush()
  }
  
  def writeDgram(s: SyslogMsg) = writeLine(s.toString)
   
}