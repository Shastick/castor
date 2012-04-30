package processer
import processer.crypt.AESProcesser
import processer.crypt.RSAProcesser
import processer.input.LogFileInput
import processer.output.MessageSync
import util.ManagedKeyStore
import javax.crypto.Cipher

object LogDecoder extends Application {

	val mk = ManagedKeyStore.load("keystore", "dorloter")
	
	val screen = new MessageSync
	screen.start
	val rsa_proc = new RSAProcesser(screen,mk.readKey("rsa_2","dorloter"), Cipher.DECRYPT_MODE)
	rsa_proc.start
	val aes_proc = new AESProcesser(screen,mk.readKey("aes_test",""),Cipher.DECRYPT_MODE)
	aes_proc.start
	
	val file_reader = new LogFileInput(rsa_proc,"test_out.txt")
	file_reader.start
	
}