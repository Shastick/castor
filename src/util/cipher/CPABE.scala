package util.cipher
import java.io.FileWriter
import java.io.FileReader
import java.io.FileOutputStream
import java.io.FileInputStream

object CPABE {
	val cpabe_parent = "/opt/local/bin/"
	val c_setup = cpabe_parent + "cpabe-setup"
	val c_enc = cpabe_parent + "cpabe-enc"

	val temp_in = "tempenc"
	val temp_out = temp_in + ".cpabe"
	
	val p_key = "pub_key"
	val m_key = "master_key"
	  
	val r = Runtime.getRuntime
	
	def setup() = r.exec(c_setup)
	
	def encrypt(in: Array[Byte], policy: String):Array[Byte] = {
	  val fw = new FileOutputStream(temp_in)
	  fw.write(in)
	  fw.flush()
	  fw.close()
	  val q = Array(p_key, temp_in, policy)
	  val p = r.exec(c_enc, q)
	

	  //val proc = r.exec(c_enc + " " + p_key + " " + temp_in + " " + policy)
	  
	  //val fr = new FileInputStream(temp_out)
	  
	  Array.empty
	}
	
	def decrypt(in: Array[Byte], policy: String): Array[Byte] = {
	  val fr = new FileInputStream(temp_out)
	  
	  Array.empty
	}
}