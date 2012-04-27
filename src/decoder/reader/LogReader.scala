package decoder.reader
import util.messages.SyslogMsg

/**
 * Dump trait representing anything able to read log data.
 */
trait LogReader extends Iterator[String]{

}