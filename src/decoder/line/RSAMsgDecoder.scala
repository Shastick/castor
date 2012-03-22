package decoder.line
import decoder.reader.LogReader
import util.SyslogMsg
import java.security.KeyStore

class RSAMsgDecoder(ks: KeyStore, lines: LogReader) extends MsgDecoder(lines) {

  def nextMsg():SyslogMsg = {
    null
  }
}