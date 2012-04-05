package digester.processer
import java.security.KeyStore
import util.cipher.RSACipher
import digester.writer.LineWriter
import util.SyslogMsg


class RSAProcesser(lw: LineWriter, ks: KeyStore, ka: String) extends LogProcesser(lw) {
	
	val cipher = RSACipher.initEncryptionCipher(ks,ka)
	def crunchArray(bytes: Array[Byte]):Array[Byte]= cipher.crunchArray(bytes)
	def writeDgram(m: SyslogMsg) = lw.writeDgram(m)
}