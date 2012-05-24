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
 * 
 * the 'apply' method call is VITAL, as it will return the
 * defined object.
 */

/**
 * Output
 * To show everything on console, just use a ScreenConfig
 * To only show errors (parse errors or verification errors),
 * 	use ErrorScreenConfig
 * To only show meta-messages(verifications successes and errors, ...), use the AdminScreenConfig
 */
val out = new ScreenConfig apply
val adminOut = new AdminScreenConfig apply
val errorOut = new ErrorScreenConfig apply

/**
 * Processing (Decrypt & Verify)
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
  keyAlias = "rsa_2"
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
  next = out
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
  handlers = Set(out,ibaAuth,hasher,file_in)
}