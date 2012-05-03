package util.cpabe
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream

/**
 * This is a custom reimplementation of the Common class contained in 
 * Junwei Wang's cpabe library:
 * 
 * http://wakemecn.github.com/cpabe/
 * 
 * It is intended to provide direct byte array interactions without
 * writing/reading through files for encryption and decryption.
 */

object Common {
  
	def suckFile(inputfile: String): Array[Byte] = {
		val is = new FileInputStream(inputfile)
		val size = is.available()
		val content = new Array[Byte](size)

		is.read(content)

		is.close()
		content
	}

	/* write byte[] into outputfile */
	def spitFile(outputfile: String, b: Array[Byte]) {
		val os = new FileOutputStream(outputfile)
		os.write(b)
		os.close()
	}


	def makeCpabeData(mBuf: Array[Byte],
			cphBuf: Array[Byte], aesBuf: Array[Byte]): Array[Byte] = {
		
		val os = new ByteArrayOutputStream
		println(aesBuf.length + " " + cphBuf.length + " " + mBuf.length)
		/* write m_buf */
		for (i <- 3 to 0)
			os.write(((mBuf.length & (0xff << 8 * i)) >> 8 * i))
		os.write(mBuf)

		/* write aes_buf */
		for (i <- 3 to 0)
			os.write(((aesBuf.length & (0xff << 8 * i)) >> 8 * i))
		os.write(aesBuf)

		/* write cph_buf */
		for (i <- 3 to 0)
			os.write(((cphBuf.length & (0xff << 8 * i)) >> 8 * i))
		os.write(cphBuf)

		os.close
		os.toByteArray
	}

	def decodeCpabeData(encdata: Array[Byte]): (Array[Byte],Array[Byte],Array[Byte]) = {
		
		val is = new ByteArrayInputStream(encdata)
		
		/* read m buf */
		var len = 0
		for (i <- 3 to 0 by -1){
			len |= is.read() << (i * 8)
		}
		
		var mBuf = new Array[Byte](len)

		println("Inside length: " + mBuf.length)
		/* read aes buf */
		len = 0
		for (i <- 3 to 0 by -1)
			len |= is.read() << (i * 8)
		
		val aesBuf = new Array[Byte](len)
		is.read(aesBuf)

		/* read cph buf */
		len = 0
		for (i <- 3 to 0 by -1)
			len |= is.read() << (i * 8)
	
		val cphBuf = new Array[Byte](len)
		is.read(cphBuf)

		is.close()
		println(aesBuf.length + " " + cphBuf.length + " " + mBuf.length)
		(aesBuf, cphBuf, mBuf)
	}
}