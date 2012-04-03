package decoder.line
import util.cipher.AESCipher
import java.security.KeyStore
import decoder.reader.LogReader
import util.SyslogMsg
import util.SyslogParser
import util.Stringifier
import util.SyslogHeader

/**
 * Decoder handling AES messages decryption.
 *  - ka is the key alias,
 *  - kp is the key password. 
 */
class AESMsgDecoder(ks: KeyStore, lines: LogReader, ka: String, kp:String)
    	extends MsgDecoder(lines) {
  
  val cipher = AESCipher.initDecryptionCipher(ks,ka,kp)
}