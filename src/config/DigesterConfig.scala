package config
import com.twitter.util.Config
import javax.crypto.Cipher
import processer.crypt.AESProcesser
import processer.crypt.RSAProcesser
import processer.input.LogFileInput
import processer.input.UDPInput
import processer.output.LogFileWriter
import processer.output.Screen
import processer.Handler
import processer.Processer
import util.ManagedKeyStore
import scala.actors.Actor

/**
 * Outputs
 */
class ScreenConfig extends Config[Screen] {
  lazy val apply = new Screen
}

class LogFileOutputConfig extends Config[LogFileWriter] {
  var destination = required[String]
  var line_separator = optional[String]
  
  lazy val apply = new LogFileWriter(destination,line_separator)
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
 * Processers
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
  
  val ks = keystore.value
  val pass = keypass.getOrElse("")
}

class AESConfig extends CipherConfig[AESProcesser] {
  
  lazy val apply = 
    new AESProcesser(next,
        ks.readKey(
            keyAlias,
            pass),
            defMode)
 
}

class RSAConfig extends CipherConfig[RSAProcesser] {
  lazy val apply = {
    var k = if(defMode == Cipher.ENCRYPT_MODE) ks.readCert(keyAlias).getPublicKey
    		else ks.readKey(keyAlias,pass)
    new RSAProcesser(next,k,defMode)
  }
}
/**
 * Utils
 */
class KeystoreConfig extends Config[ManagedKeyStore] {
  var location = required[String]
  var password = optional[String]
  
  lazy val apply: ManagedKeyStore = ManagedKeyStore.load(location, password.getOrElse(""))
}

class HandlerSet extends Config[Set[Actor]] {
  var handlers = required[Set[Actor]]

  def apply() = handlers
}