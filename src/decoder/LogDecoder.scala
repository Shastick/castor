package decoder
import util.ManagedKeyStore
import decoder.reader.LogFileReader
import decoder.line.AESMsgDecoder

object LogDecoder extends Application {
	val mk = ManagedKeyStore.load("keystore", "dorloter")
	
	val rd = new LogFileReader("test_out.txt")
	val dec = new AESMsgDecoder(mk.ks,rd,"aes_test","")
	
	while (dec.hasNext) println(dec.next)
	
	exit(0)
}