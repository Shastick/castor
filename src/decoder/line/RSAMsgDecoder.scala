package decoder.line
import decoder.reader.LogReader
import util.SyslogMsg
import java.security.KeyStore
import util.cipher.RSACipher
import util.Stringifier

class RSAMsgDecoder(ks: KeyStore, lines: LogReader, ka: String) extends MsgDecoder(lines) {
	val cipher = RSACipher.initDecryptionCipher(ks,ka,"dorloter")
	
	override def decrypt(bytes: Array[Byte]):Array[Byte] = cipher.crunchArray(bytes)
}