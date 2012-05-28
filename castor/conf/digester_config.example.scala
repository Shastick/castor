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

val file_out = new FileOutputConfig {
  destination = "test_out.txt"
} apply

/**
 * Processing (Crypt & Auth)
 */

val aes = new AESConfig {
  next = out
  mode = "ENC"
  keystore = ks
  keyAlias = "aes_test"
} apply

val rsa = new RSAConfig {
  next = out
  mode = "ENC"
  keystore = ks
  keyAlias = "rsa_2"
} apply

val cpabe_enc = new CPABEEncConfig {
  next = out
  publicKey = "files/pub_key"
} apply

val keyGen = new KeyRefillerConfig {
  keystore = ks
} apply

val hmacAuth = new HMACSignerConfig {
  secret = "retolrod"
  quantity = 10
} apply

val ibaAuth = new IBASignerConfig {
  quantity = 10
  keystore = ks
  keyAlias = "iba_testing"
  refiller = keyGen 
} apply

val hasher = new IBAHasherConfig {
  next = out
  auth = ibaAuth
} apply

/**
 *  Scheduling : the scheduler take a hasher slave and will
 *  send a 'SaveState' message every <interval> seconds.
 */
val sched = new HashSchedulerConfig {
  slave = hasher
  interval = 60
} apply

/**
 * Input
 */
val file_in = new LogFileInputConfig {
  source = "test_out.txt"
  next = out
} apply

val udp = new UDPConfig {
  next = hasher
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
  handlers = Set(out, keyGen, hmacAuth, ibaAuth, hasher, sched, udp)
}