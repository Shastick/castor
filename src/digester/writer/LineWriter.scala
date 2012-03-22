package digester.writer
import util.SyslogMsg

trait LineWriter {
  /** 
   * Children should just take an input, make sure it is a string
   * and append it "as is" to whatever their output is,
   * while making sure the line is ended.
   */

	def writeLine(str: String)
	def writeLine(bytes: Array[Byte])
	def writeDgram(s: SyslogMsg)
	
}