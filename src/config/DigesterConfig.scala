package config
import com.twitter.util.Config
import javax.crypto.Cipher
import processer.crypt.AESProcesser
import processer.crypt.RSAProcesser
import processer.crypt.CPABEProcesserDec
import processer.crypt.CPABEProcesserEnc
import processer.input.LogFileInput
import processer.input.UDPInput
import processer.output.LogFileWriter
import processer.output.Screen
import processer.Handler
import processer.Processer
import util.ManagedKeyStore
import scala.actors.Actor
import processer.auth.IBAuthenticator
import util.hasher.IBAKeyGen
import java.security.MessageDigest
import java.util.Random
import java.security.interfaces.RSAPublicKey
import processer.auth.Authenticator
import util.ScheduleManager
import processer.auth.Hasher
import util.messages.SaveState

/**
 * Outputs
 */
class ScreenConfig extends Config[Screen] {
  lazy val apply = new Screen
}

class FileOutputConfig extends Config[LogFileWriter] {
  var destination = required[String]
  var line_separator = optional[String]
  
  lazy val apply = new LogFileWriter(destination,line_separator.getOrElse("\n"))
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
 */

class IBHasherConfig extends Config[Hasher] {
  var next = required[Handler]
  var quantity = required[Int] // How many one-time keys to generate
  var keystore = required[ManagedKeyStore] // the keystore that will hold the public verification key
  var keyAlias = required[String] // Alias under which the public key will be stored
  var bitSize = optional[Int] 
  
  
  lazy val bits = bitSize.getOrElse(2048)
  lazy val (pub,priv) = keystore.genKeyPair(bits)
  
  lazy val keys = IBAKeyGen.genKeys(priv,quantity).toIterator
  
  lazy val digest = MessageDigest.getInstance("SHA-512")
  
  lazy val apply = {
    keystore.storePublicKey(keyAlias,pub)
    //TODO : possible to ensure the private key is deleted ?
    val auth = new IBAuthenticator(keys, new Random, pub, digest)
    new Hasher(next, digest, auth)
  }
}

class IBVerifierConfig extends Config[Hasher] {
  var next = required[Handler]
  var keystore = required[ManagedKeyStore]
  var keyAlias = required[String]
  
  lazy val pub = keystore.readCert(keyAlias).getPublicKey.asInstanceOf[RSAPublicKey]
  lazy val digest = MessageDigest.getInstance("SHA-512")
  
  lazy val apply = {
    val auth = new IBAuthenticator(Iterator.empty, null, pub, digest)
    new Hasher(next, digest, auth)
  }
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