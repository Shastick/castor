package digester
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.KeyPair
import java.util.Random
import util.BASE64
import util.Stringifier

import uk.ac.ic.doc.jpair.pairing.Predefined
import uk.ac.ic.doc.jpair.ibe.BFCipher
import uk.ac.ic.doc.jpair.ibe.key.BFUserPublicKey
import uk.ac.ic.doc.jpair.ibe.key.BFMasterPublicKey
import uk.ac.ic.doc.jpair.ibe.key.BFMasterPrivateKey
import uk.ac.ic.doc.jpair.ibe.key.BFUserPrivateKey
import uk.ac.ic.doc.jpair.ibe.BFCtext
import uk.ac.ic.doc.jpair.ibe.Util

/**
 * Observations : it seems that the public key is required in order to do the decryption.
 * (At least under the Jpair's implementation).
 * Worse, the master public key is stored along with the user public keys => it WILL
 * be on the system !
 */

object ibe_test extends App{
	// define a source of randomness
	val rnd = new Random
	// Define a pairing (use a default one for now), using the non super-singular curve
	val p = Predefined.nssTate	
	// Create a new Master Key Pair
	val mkp = BFCipher.setup(p,rnd)
	//First way to generate a public key.
	//It seems casting is required, could not find an alternate way at the moment.
	val pub1 = new BFUserPublicKey("User1",mkp.getPublic.asInstanceOf[BFMasterPublicKey])
	
	/** Second way to generate a public (and private) pair.
	 * (compute a pair simultaneously)
	 */ 
	val ukp2 = BFCipher.extract(mkp, "User2", rnd)
	
	//encrypt something
	val citext =
	  BFCipher.encrypt(ukp2.getPublic.asInstanceOf[BFUserPublicKey],"PONEY".getBytes(),rnd)
	
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
	
	def buildPrivateKey(mk: KeyPair, id: String, rnd: Random): BFUserPrivateKey = {
	  	val e = mk.getPublic.asInstanceOf[BFMasterPublicKey].getPairing
		//user private key: hash(ID)->point Q
		//sQ, s is the master private key
		
		val bid = Stringifier.toBytes(id)

		val Q = Util.hashToPoint(bid,e.getCurve(), e.getCofactor)
		val s = mk.getPrivate.asInstanceOf[BFMasterPrivateKey].getKey
		val Qs = e.getCurve.multiply(Q, s)
		
		new BFUserPrivateKey(Qs, mk.getPublic.asInstanceOf[BFMasterPublicKey])
	}
}