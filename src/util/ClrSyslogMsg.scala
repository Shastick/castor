package util
import scala.util.matching.Regex
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.InputStream
import java.io.ByteArrayInputStream

/**
 * ClrSyslogMsg represents a syslog message with all its elements as cleartexts.
 * 
 * Syslog datagram format
 * (PRI)(TIMESTAMP\sHOSTNAME)\s(MSG)
 */

class ClrSyslogMsg(pri: String, header: SyslogHeader, msg: String) 
	extends SyslogMsg(pri, header, msg)
