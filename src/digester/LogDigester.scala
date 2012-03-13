package digester
import digester.reader.UDPReader
import digester.processer.AESProcesser
import digester.writer.LogFileWriter
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import digester.processer.RSAProcesser
import digester.processer.ManagedKey

object LogDigester extends Application {
	val ks = ManagedKey.loadKeystore("keystore")
	ManagedKey.initKey(ks,"pony_key",256,"")
	
	val proc = new RSAProcesser(ks)
	val writer = new LogFileWriter("test_out.txt",new BASE64Encoder())
	val tester = new UDPReader(5555,proc,writer)
	tester.start
}