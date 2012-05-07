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

/**
 * Outputs
 */
class ScreenConfig extends Config[Screen] {
  def apply() = new Screen
}

class LogFileOutputConfig extends Config[LogFileWriter] {
  val destination = required[String]
  val line_separator = optional[String]
  
  def apply() = new LogFileWriter(destination,line_separator)
}
/**
 * Inputs
 */
class UDPConfig extends Config[UDPInput] {
  val port = required[Int]
  val next = required[Handler]
  
  def apply(): UDPInput = new UDPInput(port,next)
}

class LogFileInputConfig extends Config[LogFileInput] {
  val source = required[String]
  val next = required[Handler]
  
  def apply() = new LogFileInput(next, source)
}

/**
 * Processers
 */
abstract class CipherConfig[T] extends Config[T] {
  val next = required[Handler]
  val mode = required[String]
  val keystore = optional[ManagedKeyStore]
  val alias = required[String]
  val keypass = optional[String]
  
  lazy val defMode = mode.value match {
      case "ENC" => Cipher.ENCRYPT_MODE
      case "DEC" => Cipher.DECRYPT_MODE
      case _ => throw new Exception("Encryption mode can only be ENC or DEC.")
    }
  lazy val ks = keystore.get
  lazy val pass = keypass.getOrElse("")
}

class AESConfig extends CipherConfig[AESProcesser] {
  def apply() = 
    new AESProcesser(next,ks.readKey(alias,pass), defMode)
 
}

class RSAConfig extends CipherConfig[RSAProcesser] {
  def apply() = {
    val k = if(defMode == Cipher.ENCRYPT_MODE) ks.readCert(alias).getPublicKey
    		else ks.readKey(alias,pass)
    new RSAProcesser(next,k,defMode)
  }
}
/**
 * Utils
 */
class KeystoreConfig extends Config[ManagedKeyStore] {
  val location = required[String]
  val password = optional[String]
  
  def apply() = ManagedKeyStore.load(location, password.getOrElse(""))
}

object HandlerSet extends Config[Set[Handler]] {
  val handlers = required[Set[Handler]]
  def apply() = handlers
}