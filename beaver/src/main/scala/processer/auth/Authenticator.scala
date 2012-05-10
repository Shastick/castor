package processer.auth
import util.messages.AdminMsg
import util.messages.HashState
import scala.actors.Actor
import util.messages.SigRequest
import util.messages.AuthRequest
import util.messages.IBAKeys

/**
 * Represents an entity able to authenticate data.
 */
trait Authenticator extends Actor {
	/**
	 * Authenticate a byte array. The ID will be used to determine what 
	 * secret/key/whatever was used to authenticate if necessary.
	 */
	def sign(data: Array[Byte]): HashState
	
	def authenticate(s: HashState): AdminMsg
	
	def addKeys(t: (String, Iterator[(String, BigInt)]))
	
	def act() = loop {
	  react {
	    case SigRequest(a) => reply(sign(a))
	    case AuthRequest(m) => reply(authenticate(m))
	    case IBAKeys(t) => addKeys(t)
	  }
	}
}