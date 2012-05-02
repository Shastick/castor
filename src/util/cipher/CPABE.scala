package util.cipher
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.BufferedReader

object CPABE {
	val enc_script = "./abe-enc.sh"
	val dec_script = "./abe-dec.sh"
	  
	val r = Runtime.getRuntime
	
	//def setup() = r.exec(c_setup)
	
	def encrypt(in: String):Array[Byte] = {
	  /*
	  val fw = new FileOutputStream(temp_in)
	  fw.write(in)
	  fw.flush()
	  fw.close()
	  */
	  val q = Array[String](in, "4", "poney")
	  val p = r.exec(enc_script, q)
	  val isr = new InputStreamReader(p.getInputStream)
	  val bfr = new BufferedReader(isr)
	  	println(bfr.readLine())

	  //val proc = r.exec(c_enc + " " + p_key + " " + temp_in + " " + policy)
	  
	  //val fr = new FileInputStream(temp_out)
	  
	  Array.empty
	}
	
	def decrypt(in: Array[Byte], policy: String): Array[Byte] = {
	  //val fr = new FileInputStream(temp_out)
	  
	  Array.empty
	}
}