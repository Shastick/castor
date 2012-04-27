package decoder
import util.ManagedKeyStore
import decoder.reader.LogFileReader
import digester.crypt.RSAProcesser
import digester.crypt.AESProcesser
import javax.crypto.Cipher

object LogDecoder extends Application {

	val mk = ManagedKeyStore.load("keystore", "dorloter")
	
	val screen = new MessageSync
	screen.start
	val rsa_proc = new RSAProcesser(screen,mk.readCert("rsa_2").getPublicKey, Cipher.DECRYPT_MODE)
	rsa_proc.start
	val aes_proc = new AESProcesser(screen,mk.readKey("aes_test",""),Cipher.DECRYPT_MODE)
	aes_proc.start
	
	val file_reader = new LogFileReader(rsa_proc,"test_out.txt")
	file_reader.start
	
}