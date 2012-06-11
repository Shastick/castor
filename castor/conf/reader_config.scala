/**
 * Import the configuration definitions
 */
import config._

/**
 * Import the keyfile(s)
 * (if no password is set, it is asked interactively at startup)
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

val cpabe_dec = new CPABEDecConfig {
  next = out
  publicKey = "files/pub_key"
  privateKey = "files/priv_key"
} apply

/**
 * Input
 */
val file_in = new LogFileInputConfig {
  source = "test_out.txt"
  next = cpabe_dec
} apply

/**
 * Define the Set containing the previously defined elements
 * (important : that's what is then used to bootstrap everything :-))
 * and no 'apply' is required here.
 * 
 * DOUBLE CHECK the entries here if Castor does not work as expected.
 */
new HandlerSet {
  handlers = Set(file_in, cpabe_dec, out)
}
