package util.cpabe
import bswabe._
import cpabe.Cpabe
import cpabe.AESCoder
import cpabe.{Common => Util}
import java.io.ByteArrayInputStream

/**
 * Scala re-implementation and wrapping of Junwei Wang's cpabe library:
 * 
 * http://wakemecn.github.com/cpabe/
 */
class DirectCPABE {

	val cp = new Cpabe

	def setup(pubfile: String, mskfile: String) = cp.setup(pubfile,mskfile)
	def keygen(pubfile: String, prvfile: String, mskfile: String, attr: String) = 
	  cp.keygen(pubfile,prvfile,mskfile, attr)

	

	def enc(pubfile: String, policy: String, inputdata: Array[Byte]): Array[Byte] = {

		val pub_byte = Util.suckFile(pubfile)
		val pub = SerializeUtils.unserializeBswabePub(pub_byte)
		

		val keyCph = Bswabe.enc(pub, policy)
		val cph = keyCph.cph
		val m = keyCph.key

		if (cph == null) throw new Exception("CPABE Encryption error !")
		
		val cphBuf = SerializeUtils.bswabeCphSerialize(cph)

		val aesBuf = AESCoder.encrypt(m.toBytes(), inputdata)
		
		Util.writeCpabeData(m.toBytes(), cphBuf, aesBuf).toByteArray
	}

	def dec(pubfile: String, prvfile: String, decdata: Array[Byte]): Array[Byte] = {
		
		val pub_byte = Util.suckFile(pubfile)
		val pub = SerializeUtils.unserializeBswabePub(pub_byte)

		/* read ciphertext */
		val split = Util.readCpabeData(new ByteArrayInputStream(decdata))
		
		val (aesBuf, cphBuf, mBuf) = (split(0),split(1),split(2))
		
		val cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf)

		/* get BswabePrv form prvfile */
		val prv_byte = Common.suckFile(prvfile)
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