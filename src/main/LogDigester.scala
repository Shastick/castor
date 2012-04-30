package processer
import processer.output.Screen
import java.security.MessageDigest
import util.ManagedKeyStore
import processer.auth.IBAuthenticator
import processer.input.UDPInput
import processer.auth.Hasher
import util.hasher.IBAKeyGen
import java.util.Random
import util.ScheduleManager
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
	
	/*
	val writer = new LogFileWriter("test_out.txt")
	writer.start
	*/
	
	val screen = new Screen
	screen.start
	
	val (pub,priv) = IBAKeyGen.genKeyPair(2048)
	val digest = MessageDigest.getInstance("SHA-512")
	val auth_check = new IBAuthenticator(Iterator.empty, new Random, pub, digest)
	
	val auther = new Hasher(screen, digest, auth_check)
	auther.start
	
	val kl = IBAKeyGen.genKeys(priv,10)
	val auth = new IBAuthenticator(kl.toIterator,new Random,pub,digest)
	val hasher = new Hasher(auther, digest, auth)
	hasher.start
	
	/*
	val rsa_proc = new RSAProcesser(hasher,mk.readCert("rsa_2").getPublicKey, Cipher.ENCRYPT_MODE)
	rsa_proc.start
	val aes_proc = new AESProcesser(hasher,mk.readKey("aes_test",""),Cipher.ENCRYPT_MODE)
	aes_proc.start
	*/
	
	val tester = new UDPInput(5555,hasher)
	tester.start
	
	// Schedule regular state saving:
	val sm = ScheduleManager.scheduler(10000){hasher ! SaveState}
	
}