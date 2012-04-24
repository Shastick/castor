package digester

import digester.crypt._
import digester.input.UDPInput
import digester.writer.LogFileWriter
import util.ManagedKeyStore

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
	val rsa_proc = new RSAProcesser(writer,mk.ks,"rsa_2")
	rsa_proc.start
	val aes_proc = new AESProcesser(writer,mk.ks,"aes_test", "")
	aes_proc.start
	val gamal_proc = new ElGamalProcesser(writer)
	gamal_proc.start
	val tester = new UDPInput(5555,rsa_proc)
	tester.start
}