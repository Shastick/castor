package decoder.line
import decoder.reader.LogReader
import util.SyslogMsg
import util.Stringifier
import util.SyslogHeader
import javax.crypto.Cipher
import util.SyslogParser

/**
 * Abstract class setting the basics for anything wishing to decrypt log lines.
 */

abstract class MsgDecoder(log: LogReader) {
	
	val cipher: Cipher
	
	def nextMsg():SyslogMsg =
  	decipher(SyslogParser.parseCipherText(log.next))
	
	def decipher(c: SyslogMsg):SyslogMsg =
    new SyslogMsg(cf(c.pri)
        ,new SyslogHeader(cf(c.header.tstamp),cf(c.header.host))
        ,cf(c.msg))
  /**
   * cf => crunchField : take a field, and if it is a byte array, decipher it and render
   * it as a string.
   */
  private def cf(f: Either[String,Array[Byte]]) = crunchField(f)
  private def crunchField(f: Either[String,Array[Byte]]):Left[String,Array[Byte]] =
    f match {
    	case l: Left[String, Array[Byte]] => l
    	case r: Right[String, Array[Byte]] =>
    	  Left(Stringifier(cipher.doFinal(r.right.get)))
  	}
}