package util.cipher
import java.security.KeyStore

trait LogCipher {
	def crunchArray(input: Array[Byte]):Array[Byte]
}