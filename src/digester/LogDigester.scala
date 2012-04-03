package digester

import digester.input.UDPInput
import digester.processer._
import digester.writer.LogFileWriter
import util.ManagedKeyStore
import javax.crypto.Cipher


object LogDigester extends Application {
	val mk = ManagedKeyStore.load("keystore", "dorloter")
	//mk.newAESKey("aes_test",256,"")
	//mk.save
	
	val rsa_proc = new RSAProcesser(mk.ks,"rsa_2")
	val aes_proc = new AESProcesser(mk.ks,"aes_test", "")
	val gamal_proc = new ElGamalProcesser()
	val writer = new LogFileWriter("test_out.txt")
	val tester = new UDPInput(5555,gamal_proc,writer)
	tester.start
}