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
val out = new ScreenConfig apply

val file_out = new FileOutputConfig {
  destination = "test_out.txt"
} apply

/**
 * Processing (Crypt, Auth and scheduling)
 */

val keyGen = new KeyRefillerConfig {
  keystore = ks
} apply

val hmacAuth = new HMACSignerConfig {
  secret = "retolrod"
  quantity = 10
} apply

val ibaAuth = new IBASignerConfig {
  quantity = 20
  keystore = ks
  refiller = keyGen 
} apply

val hasher = new IBAHasherConfig {
  next = file_out
  auth = ibaAuth
} apply

val sched = new HashSchedulerConfig {
  slave = hasher
  interval = 20
} apply

val aes = new AESConfig {
  next = out
  mode = "ENC"
  keystore = ks
  keyAlias = "aes_test"
} apply

val rsa = new RSAConfig {
  next = hasher
  mode = "ENC"
  keystore = ks
  keyAlias = "rsa_3"
} apply

val cpabe_enc = new CPABEEncConfig {
  next = hasher
  publicKey = "files/pub_key"
} apply

/**
 * Input
 */

val file_in = new LogFileInputConfig {
  source = "test_out.txt"
  next = out
} apply

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
  handlers = Set(file_out, cpabe_enc, hasher, sched, ibaAuth, keyGen, udp)
}