package decoder

class LineDecoder(log: LogReader) {
	def nextLine = log.next()
}