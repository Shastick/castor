package digester.hash

/**
 * Represents an entity able to authenticate data.
 */
trait Authenticator {
	/**
	 * Authenticate a byte array. The ID will be used to determine what 
	 * secret/key/whatever was used to authenticate if necessary.
	 */
	def authenticate(id: String, data: Array[Byte]): (String,Array[Byte])
}