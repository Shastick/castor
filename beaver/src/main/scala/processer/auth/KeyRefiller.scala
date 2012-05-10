package processer.auth
import scala.actors.Actor
import util.ManagedKeyStore
import util.messages.IBARefill
import util.messages.IBAKeys
import util.hasher.IBAKeyGen
import java.util.Random
import util.BASE64

class KeyRefiller(ks: ManagedKeyStore) extends Actor {
  val keySize = 2048
  val rnd = new Random()
  
  def act() = loop {
		receive {
		  case IBARefill(n) => sender ! genIBA(n)
		}
	}
  
  def genIBA(q: Int): IBAKeys = {
    val (pub,priv) = ks.genKeyPair(keySize)
    val keys = IBAKeyGen.genKeys(priv, q).toIterator
    val alias = makeAlias
    ks.storePublicKey(alias,pub)
    ks.save
    IBAKeys((makeAlias,keys))
  }
  
  def makeAlias(): String = {
    var alias = ""
    do {
    	var r = new Array[Byte](4)
    	rnd.nextBytes(r)
    	alias = "auth_pubkey_" + BASE64.enc(r)
    } while(ks.contains(alias))
    alias
  }
}