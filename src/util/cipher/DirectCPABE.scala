package util.cipher

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.BufferedReader
import cpabe.Cpabe
import cpabe.AESCoder
import bswabe.SerializeUtils
import cpabe.Common
import bswabe.Bswabe
import java.io.ByteArrayInputStream
import processer.Processer

class DirectCPABE(pubKey: Array[Byte]) extends LogCipher{
  
  
  
  private def enc(policy: String, inputdata: Array[Byte]): Array[Byte] = {

		val pub_byte = pubKey
		val pub = SerializeUtils.unserializeBswabePub(pub_byte)
		

		val keyCph = Bswabe.enc(pub, policy)
		val cph = keyCph.cph
		val m = keyCph.key

		if (cph == null) throw new Exception("CPABE Encryption error !")
		
		val cphBuf = SerializeUtils.bswabeCphSerialize(cph)

		val aesBuf = AESCoder.encrypt(m.toBytes(), inputdata)
		
		Common.writeCpabeData(m.toBytes(), cphBuf, aesBuf).toByteArray
	}

  def dec(privKey: Array[Byte], decdata: Array[Byte]): Array[Byte] = {
		
		val pub_byte = pubKey
		val pub = SerializeUtils.unserializeBswabePub(pub_byte)

		/* read ciphertext */
		val split = Common.readCpabeData(new ByteArrayInputStream(decdata))
		
		val (aesBuf, cphBuf, mBuf) = (split(0),split(1),split(2))
		
		val cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf)

		/* get BswabePrv form prvfile */
		val prv_byte = privKey
		val prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte)

		val beb = Bswabe.dec(pub, prv, cph)
		if (beb.b) {
			// the right way
			// plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);
			// the wrong way
			AESCoder.decrypt(mBuf, aesBuf);
		} else {
			throw new Exception("Decryption error occured")
		}
	}
}

object DirectCPABE {
	val cp = new Cpabe
	
	/**
	 * Master keys generation, data written to files.
	 */
	def setup(pubfile: String, mskfile: String) = cp.setup(pubfile,mskfile)
	
	/**
	 * Private key generation, data read and written from files. 
	 */
	def keygen(pubfile: String, prvfile: String, mskfile: String, attr: String) = 
	  cp.keygen(pubfile,prvfile,mskfile, attr)
	  
	/**
	 * Create a DirectCPABE cipher.
	 */
	def create()
}