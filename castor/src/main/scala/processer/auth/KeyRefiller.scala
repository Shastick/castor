package processer.auth
import scala.actors.Actor
import util.ManagedKeyStore
import util.messages.IBARefill
import util.messages.IBAKeys
import util.hasher.IBAKeyGen
import java.util.Random
import util.IBAKeychain
import util.BASE64

class KeyRefiller(ks: ManagedKeyStore) extends Actor {
  
  lazy val conv = BASE64.getConverter
  
  //TODO : harmonize bit size definitions
  val keySize = 2048
  val rnd = new Random()
  
  def act() = loop {
		react {
		  case IBARefill(n) => reply(genIBA(n))
		}
	}
  
  /**
   * Generates q new one-time auth keys based on a new public/private 
   * keypair. The private key is immediately discarded while the public
   * key is stored in the keystore under a new alias.
   */
  def genIBA(q: Int): IBAKeychain = {
    val (pub,priv) = ks.genKeyPair(keySize)
    val keys = IBAKeyGen.genKeys(priv, q).toIterator
    val alias = makeAlias
    ks.storePublicKey(alias,pub)
    ks.save
    IBAKeychain(alias,pub,keys)
  }
  
  def makeAlias(): String = {
    var alias = ""
    do {
    	var r = new Array[Byte](4)
    	rnd.nextBytes(r)
    	alias = "auth_pubkey_" + conv.enc(r)
    } while(ks.contains(alias))
    alias
  }
}