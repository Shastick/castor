package digester.processer
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.SecretKeyEntry
import java.security.KeyStore.PasswordProtection
import util.AESCipher


class AESProcesser(ks: KeyStore, ka: String, kp: String) extends LogProcesser(ks,ka)  {
	
  val cipher = AESCipher.initEncryptionCipher(ks,ka,kp)

  /**
   * Encrypt a byte array
   * Currently EVERYTHING is encrypted, even if the bytes represent a one character String...
   * TODO @julien => think about it => good or bad ?
   */
  def crunchArray(input: Array[Byte]):Array[Byte]={
    cipher.doFinal(input)
  } 
}