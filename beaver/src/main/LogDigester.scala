package main
import processer.output.Screen
import processer.output.LogFileWriter
import java.security.MessageDigest
import util.ManagedKeyStore
import processer.auth.IBAuthenticator
import processer.input.UDPInput
import processer.auth.Hasher
import util.hasher.IBAKeyGen
import java.util.Random
import util.ScheduleManager
import util.messages.SaveState
import java.security.KeyPair
import java.security.interfaces.RSAPublicKey
import util.cipher.DirectCPABE
import processer.crypt.CPABEProcesserEnc
import java.io.File
import config.ScreenConfig
import com.twitter.util.Eval
import processer.Handler
import config.ScreenConfig
import scala.actors.Actor

/**
 * TODO ideas :  - ABE 
 */

object LogDigester extends App {
	//val mk = ManagedKeyStore.load("keystore", "dorloter")
	//mk.newAESKey("aes_test",256,"")
	//mk.save
  
	println("Loading configuration file...")
	val configFile = new File("digester_config.scala")
	val eval = new Eval
	println("Compiling configuration file...")
	val config = eval[com.twitter.util.Config[Set[Actor]]](configFile)
	config.validate()
	val hdlrs = config()
	println("Starting handlers...")
	hdlrs.foreach(_.start)
	println("Log Digester started.")   
}