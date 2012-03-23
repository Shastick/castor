package digester

import digester.input.UDPInput
import digester.processer.AESProcesser
import digester.writer.LogFileWriter
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import digester.processer.RSAProcesser
import util.ManagedKeyStore
import javax.crypto.Cipher

object LogDigester extends Application {
	val mk = ManagedKeyStore.load("keystore", "dorloter")
	//mk.newAESKey("aes_test",256,"")
	//mk.save
	
	val rsa_proc = new RSAProcesser(mk.ks,"rsa_test")
	val aes_proc = new AESProcesser(mk.ks,"aes_test", "")
	val writer = new LogFileWriter("test_out.txt")
	val tester = new UDPInput(5555,rsa_proc,writer)
	tester.start
}