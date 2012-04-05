package util
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.PasswordProtection
import javax.crypto.KeyGenerator
import java.security.KeyStore.SecretKeyEntry
import java.io.FileOutputStream
import java.security.SecureRandom
import java.security.KeyPairGenerator


/**
 *  Utility class for loading keystores, adding keys, etc.
 *  => TODO finish it correctly.
 */
class ManagedKeyStore(
    file: String
    ,pwd: String
    ,val ks: KeyStore) {
 
  def newAESKey(alias: String, key_size: Int, key_pass: String){
    val kgen = KeyGenerator.getInstance("AES");
    kgen.init(key_size); // 192 and 256 bits may not be available
    val skey = kgen.generateKey();
    val skeyEntry = new SecretKeyEntry(skey)
    ks.setEntry(alias,skeyEntry,new PasswordProtection(key_pass.toCharArray()))
  }
  

  def newElGamalCert(alias: String, key_size: Int, key_pass: String){
      val kpg = KeyPairGenerator.getInstance("ElGamal", "BC")
      val ran = new SecureRandom()
      kpg.initialize(160,ran) //160 is the minimum
      val pair = kpg.generateKeyPair
      val pubk = pair.getPublic
      val privk = pair.getPrivate
      
      pubk
      
  }
  
  def save = ks.store(new FileOutputStream(file), pwd.toCharArray())
}

object ManagedKeyStore {
  def load(file: String, pwd: String):ManagedKeyStore = {
    val ks = KeyStore.getInstance("JCEKS")
    ks.load(new FileInputStream(file), pwd.toCharArray())
    new ManagedKeyStore(file,pwd, ks)
  }
  
  def create(file: String, pwd: String):ManagedKeyStore = {
    val ks = KeyStore.getInstance("JCEKS")
    ks.load(null,null)
    new ManagedKeyStore(file,pwd, ks)
  }
}