/**
 * Import the configuration definitions
 */
import config._

/**
 * Import the keyfile(s)
 * (if no password is set, an empty string is used instead)
 */

val ks =  new KeystoreConfig {
  location = "keystore"
  password = "dorloter"
} apply
  
/**
 * Declare each element that will compose the log digester,
 * beginning from the output and climbing back to the input.
 */
val out = new ScreenConfig apply

val aes = new AESConfig {
  next = out
  mode = "ENC"
  keystore = ks
  keyAlias = "aes_test"
  keypass = ""
} apply
/*
val rsa = new RSAConfig {
  next = out
  mode = "ENC"
  keystore = ks
  keyAlias = "rsa_2"
} apply
*/
val file_in = new LogFileInputConfig {
  source = "test_out.txt"
  next = out
} apply

val udp = new UDPConfig {
  next = aes
  port = 5555
} apply

/**
 * Define the Set containing the previously defined elements
 * (important : that's what is then used to bootstrap everything :-))
 */
new HandlerSet {
  handlers = Set(udp,aes,out)
  keystore = ks
}