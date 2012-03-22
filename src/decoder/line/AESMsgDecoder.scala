package decoder.line
import util.AESCipher
import java.security.KeyStore
import decoder.reader.LogReader
import util.SyslogMsg
import util.SyslogParser
import util.Stringifier
import util.SyslogHeader

class AESMsgDecoder(
    ks: KeyStore, lines: LogReader, k_alias: String, k_pass:String)
    extends MsgDecoder(lines) {
  val cipher = AESCipher.initDecryptionCipher(ks,k_alias,k_pass)
}