package digester
import digester.reader.UDPReader
import digester.processer.AESProcesser

object LogDigester extends Application {
	val proc = new AESProcesser()
	val writer = new FileWriter(,new Base64Encoder)
	val tester = new UDPReader(5555,proc,writer)
	tester.start
}