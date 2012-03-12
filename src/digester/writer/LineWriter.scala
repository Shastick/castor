package digester.writer

trait LineWriter {
  /** 
   * Children should just take a string and append it "as is" to whatever their output is,
   * while making sure the line is ended.
   */

	def writeLine(str: String)
	def writeLine(bytes: Array[Byte])
	
}