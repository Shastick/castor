package decoder.line
import decoder.reader.LogReader

/**
 * Abstract class setting the basics for anything wishing to decrypt log lines.
 */

abstract class LineDecoder(log: LogReader) {
	def nextLine = log.next()
	def extractMsg(): String
}