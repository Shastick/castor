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

abstract class MsgDecoder(log: LogReader) extends Iterator[SyslogMsg]{
	
	val cipher: Cipher

	
	def hasNext = log.hasNext
	def next:SyslogMsg =
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
    	  Left(Stringifier(decrypt(r.right.get)))
  	}
	
	def decrypt(in: Array[Byte]):Array[Byte] = {
	  val pt = new Array[Byte](cipher.getOutputSize(in.size))
	  val pt_len = cipher.update(in,0,in.size,pt,0)
	  cipher.doFinal(pt,pt_len)
    pt
  }
}