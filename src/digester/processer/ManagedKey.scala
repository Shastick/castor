package digester.processer
import java.security.KeyStore
import java.io.FileInputStream
import java.security.KeyStore.PasswordProtection
import javax.crypto.KeyGenerator
import java.security.KeyStore.SecretKeyEntry

object ManagedKey {
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
}