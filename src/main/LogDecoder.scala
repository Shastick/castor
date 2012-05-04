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
import processer.output.AdminScreen
import processer.crypt.CPABEProcesserDec
import util.cipher.DirectCPABE

object LogDecoder extends App {

	val mk = ManagedKeyStore.load("keystore", "dorloter")
	
	val screen = new Screen
	screen.start
	
	
	val digest = MessageDigest.getInstance("SHA-512")
	val ver_k = mk.readCert("current").getPublicKey.asInstanceOf[RSAPublicKey]
	
	val auth = new IBAuthenticator(Iterator.empty,null,ver_k,digest)
	
	val hasher = new Hasher(screen, digest, auth)
	hasher.start
	
	
	val pubkey_l = "files/pub_key"
	val masterkey_l = "files/master_key"
	val privkey_l = "files/private_key"
	val attr = "host:mymachine pri:100 role:user"
	  
	DirectCPABE.keygen(pubkey_l,privkey_l,masterkey_l,attr)
	
	val aber = new CPABEProcesserDec(screen,pubkey_l,privkey_l)
	aber.start
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