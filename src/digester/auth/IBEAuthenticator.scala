package digester.hash
import uk.ac.ic.doc.jpair.ibe.key.BFMasterPublicKey
import uk.ac.ic.doc.jpair.ibe.key.BFUserPublicKey
import uk.ac.ic.doc.jpair.ibe.BFCipher
import java.util.Random
import uk.ac.ic.doc.jpair.ibe.BFCtext

/**
 * This class is used to 'sign' (actually encrypt) hash values in order to authenticate them.
 * It is 'sequential' in the sense that once a key has been used to encrypt a value,
 * it will be deleted.
 * 
 * This way, an attacker can theoretically not encrypt another hash if he attempts to modify log entries.
 * 
 * To check a value's authenticity, it has to be checked against the decrypted value having the same
 * sequence number.
 *
 * The SequentialCipher is built on Jpair's IBE implementation.
 */

class IBEAuthenticator(pks: Iterator[BFUserPublicKey], rnd: Random) extends Authenticator {
	
	def authenticate(id: String, data: Array[Byte]): (String,Array[Byte]) = {
	  /*if(!pks.hasNext) throw new Exception("Out of public keys !")
	  else {
		  val pk = pks.next
		  val citext = BFCipher.encrypt(pk,data,rnd)
		  (pk.gerKey,citext)
	  }*/
	  ("",Array.empty)
	}
}

object SequentialCipher {
  
}