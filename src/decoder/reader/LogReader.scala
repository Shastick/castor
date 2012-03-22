package decoder.reader
import util.SyslogMsg

/**
 * Dump trait representing anything able to read log data.
 */
trait LogReader extends Iterator[String]{
	def extractNext():SyslogMsg
}