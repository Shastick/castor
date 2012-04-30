package digester.auth
import util.messages.HashState
import util.messages.AdminMsg

/**
 * Represents an entity able to authenticate data.
 */
trait Authenticator {
	/**
	 * Authenticate a byte array. The ID will be used to determine what 
	 * secret/key/whatever was used to authenticate if necessary.
	 */
	def sign(data: Array[Byte]): HashState
	
	def authenticate(s: HashState): AdminMsg
}