package main
import processer.crypt.AESProcesser
import processer.crypt.RSAProcesser
import processer.input.LogFileInput
import processer.output.Screen
import util.ManagedKeyStore
import javax.crypto.Cipher
import java.security.MessageDigest
import processer.auth.IBASigner
import java.security.interfaces.RSAPublicKey
import java.util.Random
import processer.auth.Hasher
import processer.output.AdminScreen
import processer.crypt.CPABEProcesserDec
import util.cipher.DirectCPABE
import scala.actors.Actor
import java.io.File
import com.twitter.util.Eval

object LogDecoder extends App {
	println("Loading configuration file...")
	val configFile = new File("conf/reader_config.scala")
	val eval = new Eval
	println("Compiling configuration file...")
	val config = eval[com.twitter.util.Config[Set[Actor]]](configFile)
	println("Validating...")
	config.validate()
	println("Extracting...")
	val hdlrs = config()
	println("Starting handlers...")
	hdlrs.foreach(_.start)
	println("Log Decoder started.")  
}