package digester.processer
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.SecretKeyEntry
import java.security.KeyStore.PasswordProtection
import util.AESCipher
import util.BASE64


class AESProcesser(ks: KeyStore, ka: String, kp: String) extends LogProcesser(ks,ka)  {
	
  val cipher = AESCipher.initEncryptionCipher(ks,ka,kp)
  val d_cipher = AESCipher.initDecryptionCipher(ks,ka,kp)

  /**
   * Courtesy of 
   * http://www.java2s.com/Code/Java/Security/EncryptionanddecryptionwithAESECBPKCS7Padding.htm
   */
  def crunchArray(input: Array[Byte]):Array[Byte]={
	  val ct = new Array[Byte](cipher.getOutputSize(input.size))
	  val ct_len = cipher.update(input,0,input.size,ct,0)
	  val f_len = cipher.doFinal(ct,ct_len)
    
	 val ct_b = BASE64.dec(BASE64.enc(ct))
	  
	  val pt = new Array[Byte](d_cipher.getOutputSize(ct_b.size))
    val pt_len = d_cipher.update(ct_b,0,ct_b.size,pt,0)
    d_cipher.doFinal(pt,pt_len)
    
    println(new String(pt))
	  ct
  } 
}