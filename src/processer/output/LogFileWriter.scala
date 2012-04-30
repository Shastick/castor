package processer.output
import util.messages.SyslogMsg
import util.messages.AdminMsg
import java.io.FileWriter
import processer.Handler

/**
 * Handle writing of syslog messages to a file.
 */
class LogFileWriter(out: FileWriter, line_sep: String) extends Handler {
  
  /**
   * Overloaded constructor for a filename specified by string.
   */
  def this(fname: String) = this(new FileWriter(fname,true), "\n")
  
  def procDgram(s: SyslogMsg) = writeLine(s.toString)
  
  def procAdminMsg(m: AdminMsg) = writeLine(m.toString)
  /**
   * Write a line to the FileWriter and ensure it is terminated by a line separator.
   */
  private def writeLine(line: String) = {
    if (line.endsWith(line_sep)) out.write(line)
    	else out.write(line + line_sep)
    out.flush()
  }
}