package decoder.line
import decoder.reader.LogReader
import util.SyslogMsg
import java.security.KeyStore
import util.RSACipher

class RSAMsgDecoder(ks: KeyStore, lines: LogReader, ka: String) extends MsgDecoder(lines) {
	val cipher = RSACipher.initDecryptionCipher(ks,ka)
	
}