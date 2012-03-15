package decoder

/**
 * Abstract class setting the basics for anything wishing to decrypt log lines.
 */

abstract class LineDecoder(log: LogReader) {
	def nextCipherLine = log.next()
	
	def extractLine(): String
}