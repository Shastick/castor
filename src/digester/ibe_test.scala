package digester
import uk.ac.ic.doc.jpair.api._
import uk.ac.ic.doc.jpair.ibe._
import uk.ac.ic.doc.jpair.pairing._
import uk.ac.ic.doc.jpair.ibe.key._
import java.util.Random
import java.io.ObjectOutputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import util.BASE64

object ibe_test extends App{
	// define a source of randomness
	val rnd = new Random
	// Define a pairing (use a default one for now), using the non super-singular curve
	val p = Predefined.nssTate	
	// Create a new Master Key Pair
	val mkp = BFCipher.setup(p,rnd)
	//First way to generate a public key.
	//Nasty because creating the corresponding private key does not seem trivial for me at the moment
	//It seems casting is required, could not find an alternate way at the moment.
	val pub1 = new BFUserPublicKey("User1",mkp.getPublic.asInstanceOf[BFMasterPublicKey])
	
	/** Second way to generate a public (and private) pair.
	 * Nice because the library does everything under the hood,
	 * Bad because if I go along this lazy way, I have to store all the corresponding 
	 * decryption keys from the beginning => they should only be computed from the master
	 * private key at decryption time. => TODO check how to do this
	 */ 
	val ukp2 = BFCipher.extract(mkp, "User2", rnd)
	
	//encrypt something
	val citext =
	  BFCipher.encrypt(ukp2.getPublic().asInstanceOf[BFUserPublicKey],"PONEY".getBytes(),rnd)
	
	val a = BASE64.enc(serialize(citext))
	println(a)
	val d = deserialize(BASE64.dec(a))
	  
	val cltext = BFCipher.decrypt(d, ukp2.getPrivate().asInstanceOf[BFUserPrivateKey])
	
	println(new String(cltext))

	def serialize(c: BFCtext): Array[Byte] = {
	  val aos = new ByteArrayOutputStream
	  val oos = new ObjectOutputStream(aos)
	  
	  oos.writeObject(c)
	  oos.close
	  
	  aos.toByteArray
	}
	
	def deserialize(a: Array[Byte]): BFCtext = {
	  val ais = new ByteArrayInputStream(a)
	  val ois = new ObjectInputStream(ais)
	  
	  ois.readObject() match {
	    case c: BFCtext => c
	    case _ => throw new IOException("Not a BFCtext found at deserialization!")
	  }
	}
}