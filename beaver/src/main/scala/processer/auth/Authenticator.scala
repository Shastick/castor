package processer.auth
import util.messages.AdminMsg
import util.messages.HashState
import scala.actors.Actor
import util.messages.SigRequest
import util.messages.AuthRequest
import util.messages.IBAKeys
import java.security.interfaces.RSAPublicKey
import util.Keychain
import util.IBAKeychain
import util.BASE64

/**
 * Represents an entity able to authenticate data.
 */
trait Authenticator extends Actor {
  
	lazy val conv = BASE64.getConverter
	/**
	 * Authenticate a byte array. The ID will be used to determine what 
	 * secret/key/whatever was used to authenticate if necessary.
	 */
	def sign(data: Array[Byte]): HashState
	
	def authenticate(s: HashState): AdminMsg
	
	//TODO : wrap the notion of key into something abstract
	def addKeys(kc : Keychain)
	
	def act() = loop {
	  react {
	    case SigRequest(a) => reply(sign(a))
	    case AuthRequest(m) => reply(authenticate(m))
	    case k: IBAKeychain => addKeys(k)
	  }
	}
}