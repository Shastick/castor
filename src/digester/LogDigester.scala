package digester
import digester.reader.UDPReader
import digester.processer.AESProcesser

object LogDigester extends Application {
	val proc = new AESProcesser()
	val tester = new UDPReader(5555,proc)
	tester.start
}