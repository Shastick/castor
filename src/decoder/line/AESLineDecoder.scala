package decoder.line
import util.AESCipher
import java.security.KeyStore
import decoder.reader.LogReader

class AESLineDecoder(ks: KeyStore, lines: LogReader) extends LineDecoder(lines) {
  
  val cipher = AESCipher.initDecryptionCipher(ks,"aes_pony1","")
  
  def extractLine():String = {
	return "LOL"
  }
}