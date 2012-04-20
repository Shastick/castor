package digester.hash

/**
 * This class is used to 'sign' (actually encrypt) hash values in order to authenticate them.
 * It is 'sequential' in the sense that once a key has been used to encrypt a value,
 * it will be deleted.
 * 
 * This way, an attacker can theoretically not encrypt another hash if he attempts to modify log entries.
 * 
 * To check a value's authenticity, it has to be checked against the decrypted value having the same
 * sequence number.
 *
 * The SequentialCipher is built on Jpair's IBE implementation.
 */

class SequentialCipher {

}