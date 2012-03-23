package decoder.line
import decoder.reader.LogReader
import util.SyslogMsg
import java.security.KeyStore
import util.RSACipher

class RSAMsgDecoder(ks: KeyStore, lines: LogReader, ka: String) extends MsgDecoder(lines) {
	val cipher = RSACipher.initDecryptionCipher(ks,ka)
	
	override def decrypt(bytes: Array[Byte]):Array[Byte] = {
		
		val groups = bytes.grouped(cipher.getBlockSize())
		
		val cipher_data = groups.flatMap(b => cipher.doFinal(b))
		val out = new Array[Byte](cipher_data.size)
		cipher_data.copyToArray(out)
		out
	}
}