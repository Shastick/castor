package digester

import digester.input.UDPInput
import digester.processer._
import digester.writer.LogFileWriter
import util.ManagedKeyStore
import javax.crypto.Cipher

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
	val rsa_proc = new RSAProcesser(writer,mk.ks,"rsa_2")
	val aes_proc = new AESProcesser(writer,mk.ks,"aes_test", "")
	val gamal_proc = new ElGamalProcesser(writer)
	val tester = new UDPInput(5555,rsa_proc)
	tester.start
}