package decoder.line
import decoder.reader.LogReader
import util.SyslogMsg

/**
 * Abstract class setting the basics for anything wishing to decrypt log lines.
 */

abstract class MsgDecoder(log: LogReader) {
	def nextMsg():SyslogMsg
}