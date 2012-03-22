package decoder.line
import util.AESCipher
import java.security.KeyStore
import decoder.reader.LogReader
import util.SyslogMsg

class AESMsgDecoder(ks: KeyStore, lines: LogReader) extends MsgDecoder(lines) {
  
  val cipher = AESCipher.initDecryptionCipher(ks,"aes_test","")
  
  def nextMsg():SyslogMsg = {
		  null
  }
}