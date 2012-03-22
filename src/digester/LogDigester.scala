package digester
import digester.input.UDPInput
import digester.processer.AESProcesser
import digester.writer.LogFileWriter
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import digester.processer.RSAProcesser
import util.ManagedKey

object LogDigester extends Application {
	val ks = ManagedKey.loadKeystore("keystore")
	ManagedKey.initKey(ks,"aes_test",256,"")
	
	val proc = new AESProcesser(ks)
	val writer = new LogFileWriter("test_out.txt")
	val tester = new UDPInput(5555,proc,writer)
	tester.start
}