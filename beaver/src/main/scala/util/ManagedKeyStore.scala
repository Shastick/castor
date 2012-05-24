package util
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.KeyStore.PasswordProtection
import java.security.KeyStore.SecretKeyEntry
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyStore
import java.util.Date
import org.bouncycastle.asn1.x509.BasicConstraints
import org.bouncycastle.asn1.x509.ExtendedKeyUsage
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.asn1.x509.GeneralNames
import org.bouncycastle.asn1.x509.KeyPurposeId
import org.bouncycastle.asn1.x509.KeyUsage
import org.bouncycastle.asn1.x509.X509Extensions
import org.bouncycastle.x509.X509V3CertificateGenerator
import javax.crypto.KeyGenerator
import javax.security.auth.x500.X500Principal
import java.security.spec.RSAPrivateKeySpec
import java.security.interfaces.RSAPrivateKey
import java.security.KeyPairGenerator


/**
 *  Utility class for loading keystores, adding keys, etc.
 *  => TODO find an alternative to the deprecated method calls.
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
  
  /**
   * Store an RSA key in a certificate with a dummy private key 
   * (ugly way to only store a public key)
   * TODO: investigate trust-stores
   */
  def storePublicKey(alias: String, k: RSAPublicKey) {
	val (_,dummy) = genKeyPair(k.getEncoded.size * 8)
    val cert = makeCert(new KeyPair(k,dummy))
    addCert(alias, cert)
  }
  
  // Courtesy 
  // http://www.java2s.com/Tutorial/Java/0490__Security/CreatingaSelfSignedVersion3Certificate.htm
  def makeCert(pair: KeyPair):X509Certificate = {
   
    val certGen = new X509V3CertificateGenerator();
	    certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
	    certGen.setIssuerDN(new X500Principal("CN=Test Certificate"));
	    certGen.setNotBefore(new Date(System.currentTimeMillis() - 10000));
	    certGen.setNotAfter(new Date(System.currentTimeMillis() + 10000));
	    certGen.setSubjectDN(new X500Principal("CN=Test Certificate"));
	    certGen.setPublicKey(pair.getPublic());
	    certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

    certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
    certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature
        | KeyUsage.keyEncipherment));
    certGen.addExtension(X509Extensions.ExtendedKeyUsage, true, new ExtendedKeyUsage(
        KeyPurposeId.id_kp_serverAuth));

    certGen.addExtension(X509Extensions.SubjectAlternativeName, false, new GeneralNames(
        new GeneralName(GeneralName.rfc822Name, "test@test.test")));
    
    certGen.generateX509Certificate(pair.getPrivate(), "BC");
  }
  
  def addCert(alias: String, c: X509Certificate) {
    ks.setCertificateEntry(alias, c)
  }
  
  def readCert(alias: String) = getCert(ks, alias)
  
  def readKey(alias: String):Key = readKey(alias, "")
  def readKey(alias: String, password: String):Key = ks.getKey(alias,password.toCharArray())
  
  def readPublicKey(alias: String) = readCert(alias).getPublicKey.asInstanceOf[RSAPublicKey]
  
  def save = ks.store(new FileOutputStream(file), pwd.toCharArray())

  /**
   * Generate a new random RSA key pair.
   */
  def genKeyPair(bit_size: Int): (RSAPublicKey,RSAPrivateKey) = {
    val kg = KeyPairGenerator.getInstance("RSA", "BC")
    kg.initialize(bit_size)
    val kp = kg.genKeyPair()
    (kp.getPublic.asInstanceOf[RSAPublicKey],kp.getPrivate.asInstanceOf[RSAPrivateKey])
  }
  
  def contains(alias: String) = ks.containsAlias(alias)
  
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