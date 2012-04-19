package digester.writer
import util.SyslogMsg
import digester.LogHandler

trait LineWriter extends LogHandler{
  /** 
   * Children should just take an input, make sure it is a string
   * and append it "as is" to whatever their output is,
   * while making sure the line is ended.
   */
	def writeDgram(s: SyslogMsg)
}