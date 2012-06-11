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
} apply
  
/**
 * Declare each element that will compose the log digester,
 * beginning from the output and climbing back to the input.
 */

/**
 * Output
 */

val file_out = new FileOutputConfig {
  destination = "test_out.txt"
} apply

/**
 * Processing (Crypt, Auth and scheduling)
 */
val cpabe_enc = new CPABEEncConfig {
  next = file_out
  publicKey = "files/pub_key"
} apply

/**
 * Input
 */
val udp = new UDPConfig {
  next = cpabe_enc
  port = 5555
} apply

/**
 * Define the Set containing the previously defined elements
 * (important : that's what is then used to bootstrap everything :-))
 * and no 'apply' is required here.
 * 
 * DOUBLE CHECK the entries here if Castor does not work as expected.
 */
new HandlerSet {
  handlers = Set(file_out, cpabe_enc, udp)
}
