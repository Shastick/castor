package util.cipher
import javax.crypto.Cipher
import java.security.KeyPairGenerator
import java.security.SecureRandom


class ElGamalCipher(cipher: Cipher) extends LogCipher {
  
	def crunchArray(in: Array[Byte]):Array[Byte] = {
	  val o = new Array[Byte](cipher.getOutputSize(in.size))
	  val in_data = in.grouped(cipher.getBlockSize)
	  val proc_data = in_data.flatMap(b => cipher.doFinal(b))
	  proc_data.copyToArray(o)
	  o
	}
}

object ElGamalCipher {
	/**
	 * El Gamal is not implementend in the sun JDK,
	 * a library like BouncyCastle is required.
	 * 
	 * Random keygen courtesy 
	 * http://www.java2s.com/Tutorial/Java/0490__Security/ElGamalexamplewithrandomkeygeneration.htm
	 */
  //TODO ElGamal key storing
  val cdef = "ElGamal/ECB/PKCS1Padding"
  val provider = "BC"
  
  val kpg = KeyPairGenerator.getInstance("ElGamal", "BC")
  val ran = new SecureRandom()
  kpg.initialize(160,ran) //160 is the minimum
  
  val pair = kpg.generateKeyPair
  val pubk = pair.getPublic
  val privk = pair.getPrivate
  
  def initEncryptionCiher():ElGamalCipher = {
    val c = Cipher.getInstance(cdef,provider)
    c.init(Cipher.ENCRYPT_MODE,pubk,ran)
    new ElGamalCipher(c)
  }
  
  def initDecryptionCipher():ElGamalCipher = {
    val c = Cipher.getInstance(cdef,provider)
    c.init(Cipher.DECRYPT_MODE,privk,ran)
    new ElGamalCipher(c)
  }
}