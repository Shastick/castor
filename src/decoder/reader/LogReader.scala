package decoder.reader
import util.SyslogMsg

/**
 * Dump trait representing anything able to read log data.
 */
trait LogReader{
	def extractNext():SyslogMsg
}