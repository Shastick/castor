package main
import processer.crypt.AESProcesser
import processer.crypt.RSAProcesser
import processer.input.LogFileInput
import processer.output.Screen
import util.ManagedKeyStore
import javax.crypto.Cipher
import java.security.MessageDigest
import processer.auth.IBAuthenticator
import java.security.interfaces.RSAPublicKey
import java.util.Random
import processer.auth.Hasher

object LogDecoder extends Application {

	val mk = ManagedKeyStore.load("keystore", "dorloter")
	
	val screen = new Screen
	screen.start
	
	val digest = MessageDigest.getInstance("SHA-512")
	val ver_k = mk.readCert("current").getPublicKey.asInstanceOf[RSAPublicKey]
	
	val auth = new IBAuthenticator(Iterator.empty,new Random,ver_k,digest)
	
	val hasher = new Hasher(screen, digest, auth)
	hasher.start
	
	/*
	val rsa_proc = new RSAProcesser(screen,mk.readKey("rsa_2","dorloter"), Cipher.DECRYPT_MODE)
	rsa_proc.start
	val aes_proc = new AESProcesser(screen,mk.readKey("aes_test",""),Cipher.DECRYPT_MODE)
	aes_proc.start
	*/
	/*
	val auther = new Hasher(writer, digest, auth_check)
	auther.start
	*/
	
	val file_reader = new LogFileInput(hasher,"test_out.txt")
	file_reader.start
}