package util.cipher

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import processer.Processer
import processer.crypt.CPABEProcesserEnc
import cpabe.Common
import bswabe.Bswabe
import bswabe.SerializeUtils
import cpabe.Cpabe
import cpabe.AESCoder

class DirectCPABE(pubKey: Array[Byte]){
  
  /**
   * Alternate constructor
   */
  def this(pubkey_loc: String) = this(Common.suckFile(pubkey_loc))
  
  def enc(policy: String, inputdata: Array[Byte]): Array[Byte] = {

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

  def dec(privKey: Array[Byte], decdata: Array[Byte]): Option[Array[Byte]] = {
		
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
			// TODO : clear this out, it comes from the cpabe library
			// the right way
			// plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);
			// the wrong way
			Some(AESCoder.decrypt(mBuf, aesBuf))
		} else None
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
	  cp.keygen(pubfile, prvfile, mskfile, attr)
}