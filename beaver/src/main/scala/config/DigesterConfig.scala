package config
import java.security.interfaces.RSAPublicKey
import java.security.MessageDigest
import java.util.Random
import scala.actors.Actor
import com.twitter.util.Config
import javax.crypto.Cipher
import processer.auth.Authenticator
import processer.auth.HMACAuthenticator
import processer.auth.Hasher
import processer.auth.IBASigner
import processer.auth.IBAVerifier
import processer.auth.KeyRefiller
import processer.crypt.AESProcesser
import processer.crypt.CPABEProcesserDec
import processer.crypt.CPABEProcesserEnc
import processer.crypt.RSAProcesser
import processer.input.LogFileInput
import processer.input.UDPInput
import processer.output.LogFileWriter
import processer.output.Screen
import processer.Handler
import processer.Processer
import util.hasher.IBAKeyGen
import util.messages.SaveState
import util.ManagedKeyStore
import util.ScheduleManager
import util.hasher.HMACKeyGen
import org.bouncycastle.crypto.digests.SHA512Digest
import util.IBAKeychain
import processer.output.ErrorScreen
import processer.output.AdminScreen
import processer.output.UDPOutput


/**
 * Ostrich Configurations definition
 */

/**
 * Outputs
 */
class ScreenConfig extends Config[Screen] {
  lazy val apply = new Screen
}

class AdminScreenConfig extends Config[Screen] {
  lazy val apply = new AdminScreen
}

class ErrorScreenConfig extends Config[Screen] {
  lazy val apply = new ErrorScreen
}

/**
 * Outputs
 */
class FileOutputConfig extends Config[LogFileWriter] {
  var destination = required[String]
  var line_separator = optional[String]
  
  lazy val apply = new LogFileWriter(destination,line_separator.getOrElse("\n"))
}

class UDPOutputConfig extends Config[UDPOutput] {
  var destination = required[String]
  var port = required[Int]
  
  lazy val apply = new UDPOutput(destination,port)
}
/**
 * Inputs
 */
class UDPConfig extends Config[UDPInput] {
  var port = required[Int]
  var next = required[Handler]
  
  lazy val apply: UDPInput = new UDPInput(port,next)
}

class LogFileInputConfig extends Config[LogFileInput] {
  var source = required[String]
  var next = required[Handler]
  
  lazy val apply = new LogFileInput(next, source)
}

/**
 * Ciphers
 */
abstract class CipherConfig[T] extends Config[T] {
  var next = required[Handler]
  var mode = required[String]
  var keystore = required[ManagedKeyStore]
  var keyAlias = required[String]
  var keypass = optional[String]
  
  lazy val defMode = mode.value match {
      case "ENC" => Cipher.ENCRYPT_MODE
      case "DEC" => Cipher.DECRYPT_MODE
      case _ => throw new Exception("Encryption mode can only be ENC or DEC.")
    }
  
  val pass = keypass.getOrElse("")
}

class AESConfig extends CipherConfig[AESProcesser] {
  
  lazy val apply = 
    new AESProcesser(next, keystore.readKey(keyAlias, pass), defMode)
 
}

class RSAConfig extends CipherConfig[RSAProcesser] {
  lazy val apply = {
    var k = if(defMode == Cipher.ENCRYPT_MODE) keystore.readCert(keyAlias).getPublicKey
    		else keystore.readKey(keyAlias,pass)
    new RSAProcesser(next,k,defMode)
  }
}

class CPABEEncConfig extends Config[CPABEProcesserEnc] {
  var next = required[Handler]
  var publicKey = required[String]
  
  lazy val apply = new CPABEProcesserEnc(next, publicKey)
}

class CPABEDecConfig extends Config[CPABEProcesserDec] {
  var next = required[Handler]
  var publicKey = required[String]
  var privateKey = required[String]
  
  lazy val apply = new CPABEProcesserDec(next, publicKey, privateKey)
}

/**
 * Authenticators
 * TODO : harmonize key size definition
 */

class KeyRefillerConfig extends Config[KeyRefiller] {
  var keystore = required[ManagedKeyStore]
  
  lazy val apply = new KeyRefiller(keystore)
}

class IBAVerifierConfig extends Config[Authenticator] {
  var keystore = required[ManagedKeyStore]
  
  lazy val digest = MessageDigest.getInstance("SHA-512")
  lazy val apply = new IBAVerifier(keystore, digest)
}

class IBASignerConfig extends Config[Authenticator] {
  var quantity = required[Int] // How many one-time keys to generate initially and at refill
  var keystore = required[ManagedKeyStore] // the keystore that will hold the public verification key
  var keyAlias = required[String] // Alias under which the public key will be stored
  var refiller = required[KeyRefiller]
  
  var bitSize = optional[Int]
  
  lazy val bits = bitSize.getOrElse(2048)
  lazy val digest = MessageDigest.getInstance("SHA-512")
  
  lazy val apply = {
    println("Generating initial key list")
    val (pub,priv) = keystore.genKeyPair(bits)
    val keys = IBAKeyGen.genKeys(priv,quantity).toIterator
    keystore.storePublicKey(keyAlias,pub)
    keystore.save
    // Calling the garbage collector to make sure the private key is removed ASAP
    // Not sure if useful, but it's the only thing I see.
    System.gc
    new IBASigner(List(IBAKeychain(keyAlias, pub, keys)), new Random, digest, refiller, quantity)
  }
}

class IBAHasherConfig extends Config[Hasher] {
  var next = required[Handler]
  var auth = required[Authenticator]
  
  val digest = MessageDigest.getInstance("SHA-512")
  
  lazy val apply = new Hasher(next, digest, auth)

}

class HMACSignerConfig extends Config[HMACAuthenticator] {
  
  var secret = required[String]
  var quantity = required[Int]

  val digest = new SHA512Digest()
  
  lazy val apply =
    new HMACAuthenticator(HMACKeyGen.genKeys(secret,quantity), digest)
}

class HashSchedulerConfig extends Config[Actor] {
  var slave = required[Hasher]
  var interval = required[Int] // Interval in seconds
  
  lazy val apply = ScheduleManager.scheduler(interval){slave ! SaveState}
}

/**
 * Utils
 */
class KeystoreConfig extends Config[ManagedKeyStore] {
  var location = required[String]
  var password = optional[String]
  
  lazy val apply = ManagedKeyStore.load(location, password.getOrElse(""))
}

class HandlerSet extends Config[Set[Actor]] {
  var handlers = required[Set[Actor]]

  def apply() = handlers
}