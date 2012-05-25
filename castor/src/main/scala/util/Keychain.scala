package util
import java.security.interfaces.RSAPublicKey

abstract class Keychain

case class IBAKeychain(id: String, pub: RSAPublicKey, keys: Iterator[(String,BigInt)]) extends Keychain
case class HMACKeychain(keys: Iterator[(String,Array[Byte])]) extends Keychain