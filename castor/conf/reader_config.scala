/**
 * Import the configuration definitions
 */
import config._

/**
 * Import the keyfile(s)
 * (if no password is set, an empty string is used instead)
 */

val ks = new KeystoreConfig {
  location = "files/keystore"
  password = "dorloter"
} apply
  
/**
 * Declare each element that will compose the log digester,
 * beginning from the output and climbing back to the input.
 */

/**
 * Output
 */
val out = new ScreenConfig apply

/**
 * Processing (Crypt, Auth and scheduling)
 */

val aes = new AESConfig {
  next = out
  mode = "DEC"
  keystore = ks
  keyAlias = "aes_test"
} apply

val rsa = new RSAConfig {
  next = out
  mode = "DEC"
  keystore = ks
  keyAlias = "rsa_3"
  keypass = "dorloter"
} apply

val cpabe_dec = new CPABEDecConfig {
  next = out
  publicKey = "files/pub_key"
  privateKey = "files/private_key"
} apply

val ibaAuth = new IBAVerifierConfig {
  keystore = ks
} apply

val hasher = new IBAHasherConfig {
  next = rsa
  auth = ibaAuth
} apply

/**
 * Input
 */
val file_in = new LogFileInputConfig {
  source = "test_out.txt"
  next = hasher
} apply


/**
 * Define the Set containing the previously defined elements
 * (important : that's what is then used to bootstrap everything :-))
 * and no 'apply' is required here.
 * 
 * DOUBLE CHECK the entries here if Castor does not work as expected.
 */
new HandlerSet {
  handlers = Set(out,rsa, hasher,ibaAuth,file_in)
}