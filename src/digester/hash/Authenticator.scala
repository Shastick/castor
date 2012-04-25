package digester.hash

/**
 * Represents an entity able to authenticate data.
 */
trait Authenticator {
	def authenticate(data: Array[Byte]): Array[Byte]
}