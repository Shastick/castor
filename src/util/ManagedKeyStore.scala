package util
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.PasswordProtection
import javax.crypto.KeyGenerator
import java.security.KeyStore.SecretKeyEntry
import java.io.FileOutputStream

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
  
  def newElGamalCert(alias: String, key_site: Int, key_pass: String){
    
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