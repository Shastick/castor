package main
import processer.output.Screen
import processer.output.LogFileWriter
import java.security.MessageDigest
import util.ManagedKeyStore
import processer.auth.IBASigner
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

object LogDigester extends App {
	println("Loading configuration file...")
	val configFile = new File("conf/digester_config.scala")
	val eval = new Eval
	println("Compiling configuration file...")
	val config = eval[com.twitter.util.Config[Set[Actor]]](configFile)
	println("Validating...")
	config.validate()
	println("Extracting...")
	val hdlrs = config()
	println("Starting handlers...")
	hdlrs.foreach(_.start)
	println("Log Digester started.")   
}