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
object ManagedKeyStore(ks: KeyStore) {
 
  def initKey(ks: KeyStore, alias: String, key_size: Int, key_pass: String){
    val kgen = KeyGenerator.getInstance("AES");
    kgen.init(key_size); // 192 and 256 bits may not be available
    val skey = kgen.generateKey();
    val skeyEntry = new SecretKeyEntry(skey)
    
    ks.setEntry(alias,skeyEntry,new PasswordProtection(key_pass.toCharArray()))
  }
    
  def loadKeystore(file: String):KeyStore = {
    val keystore = KeyStore.getInstance("JCEKS")
    keystore.load(new FileInputStream(file), null)
    keystore
  }
  
  def saveKeystore(ks: KeyStore, file: String) {
    ks.store(new FileOutputStream(file),null)
  }
}