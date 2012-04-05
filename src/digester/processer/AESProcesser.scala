package digester.processer
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.SecretKeyEntry
import java.security.KeyStore.PasswordProtection
import util.cipher.AESCipher
import util.BASE64
import digester.writer.LineWriter
import util.SyslogMsg


class AESProcesser(lw: LineWriter, ks: KeyStore, ka: String, kp: String) extends LogProcesser(lw) {
	
  val cipher = AESCipher.initEncryptionCipher(ks,ka,kp)
  def crunchArray(input: Array[Byte]):Array[Byte]= cipher.crunchArray(input)

}