package util.cipher
import cpabe.Cpabe

object cpape_test extends App{
  
  val pubkey_l = "files/pub_key"
  val masterkey_l = "files/master_key"
    
  val privkey_l = "files/private_key"
  
  val cp = new Cpabe
  cp.setup(pubkey_l,masterkey_l)
  
  cp.keygen(pubkey_l,privkey_l,masterkey_l,"pri:4 role:admin host:poney")
  
  
}