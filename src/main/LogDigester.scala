package main
import processer.output.Screen
import processer.output.LogFileWriter
import java.security.MessageDigest
import util.ManagedKeyStore
import processer.auth.IBAuthenticator
import processer.input.UDPInput
import processer.auth.Hasher
import util.hasher.IBAKeyGen
import java.util.Random
import util.ScheduleManager
import util.messages.SaveState
import java.security.KeyPair
import java.security.interfaces.RSAPublicKey
import util.cipher.DirectCPABE
import processer.crypt.CPABEProcesserEnc

/**
 * TODO ideas :  - ABE 
 */

object LogDigester extends App {
	val mk = ManagedKeyStore.load("keystore", "dorloter")
	//mk.newAESKey("aes_test",256,"")
	//mk.save
	
	val writer = new LogFileWriter("test_out.txt")
	writer.start
	
	val screen = new Screen
	screen.start
	
	/*
	val (pub,priv) = IBAKeyGen.genKeyPair(2048)
	val (dum_pub,dum_priv) = IBAKeyGen.genKeyPair(2048)
	val kp = new KeyPair(pub,dum_priv)
	val cert = mk.makeCert(kp)
	mk.addCert("current",cert)
	mk.save
	
	val digest = MessageDigest.getInstance("SHA-512")
	
	val kl = IBAKeyGen.genKeys(priv,10)
	val ver_k = mk.readCert("current").getPublicKey.asInstanceOf[RSAPublicKey]
	
	val auth = new IBAuthenticator(kl.toIterator,new Random,ver_k,digest)
	
	val hasher = new Hasher(writer, digest, auth)
	hasher.start
	hasher ! SaveState
	*/
	val pubkey_l = "files/pub_key"
	val masterkey_l = "files/master_key"
    
	//DirectCPABE.setup(pubkey_l,masterkey_l)
	val aber = new CPABEProcesserEnc(screen,pubkey_l)
	aber.start
	/*
	val rsa_proc = new RSAProcesser(hasher,mk.readCert("rsa_2").getPublicKey, Cipher.ENCRYPT_MODE)
	rsa_proc.start
	val aes_proc = new AESProcesser(hasher,mk.readKey("aes_test",""),Cipher.ENCRYPT_MODE)
	aes_proc.start
	*/
	
	val tester = new UDPInput(5555,screen)
	tester.start
	
	// Schedule regular state saving:
	//val sm = ScheduleManager.scheduler(10000){hasher ! SaveState}
	//sm.start
	
}