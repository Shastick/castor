package util
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.PasswordProtection
import javax.crypto.KeyGenerator
import java.security.KeyStore.SecretKeyEntry
import java.io.FileOutputStream
import java.security.SecureRandom
import java.security.KeyPairGenerator
import java.security.cert.X509Certificate
import java.security.Key
import java.security.cert.CertPath
import java.security.cert.CertificateFactory
import java.security.PublicKey


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
  
  def storeKey(k: Key, alias: String, pwd: String) {
    ks.setKeyEntry(alias, k, pwd.toCharArray,null)
  }
  
  def readCert(alias: String) = getCert(ks, alias)
  
  def readKey(alias: String):Key = readKey(alias, "")
  def readKey(alias: String, password: String):Key = ks.getKey(alias,password.toCharArray())
  
  def save = ks.store(new FileOutputStream(file), pwd.toCharArray())
  
  private def getCert(ks: KeyStore, c_alias: String):X509Certificate =
  	ks.getCertificate(c_alias) match {
  		case c:X509Certificate => c
  		case _ => throw new Exception("Woops, not a X509Certificate loaded!")
  }
}

/**
 * Companion object
 */
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