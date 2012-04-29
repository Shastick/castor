package digester

import digester.crypt._
import digester.input.UDPInput
import digester.writer.LogFileWriter
import util.ManagedKeyStore
import javax.crypto.Cipher
import java.security.MessageDigest
import digester.auth.Hasher
import util.hasher.IBAKeyGen
import digester.auth.IBAuthenticator
import java.util.Random
import util.messages.SaveState

/**
 * TODO ideas :  - ABE 
 * 				 - Log Authentication ! => Log Chaining ! Needs a secret... 
 * 
 */

object LogDigester extends Application {
	val mk = ManagedKeyStore.load("keystore", "dorloter")
	//mk.newAESKey("aes_test",256,"")
	//mk.save
	
	val writer = new LogFileWriter("test_out.txt")
	writer.start
	
	val digest = MessageDigest.getInstance("SHA-512")
	val (pub,priv) = IBAKeyGen.genKeyPair(2048)
	val kl = IBAKeyGen.genKeys(priv,10)
	val auth = new IBAuthenticator(kl.toIterator,new Random,pub,digest)
	val hasher = new Hasher(writer, digest,auth)
	hasher.start
	
	/*
	val rsa_proc = new RSAProcesser(hasher,mk.readCert("rsa_2").getPublicKey, Cipher.ENCRYPT_MODE)
	rsa_proc.start
	val aes_proc = new AESProcesser(hasher,mk.readKey("aes_test",""),Cipher.ENCRYPT_MODE)
	aes_proc.start
	*/
	
	val tester = new UDPInput(5555,hasher)
	tester.start
	
	val sm = ScheduleManager.scheduler(10000){hasher ! SaveState}
	
}