package util.cipher
import util.cpabe.DirectCPABE
import util.BASE64
import util.Stringifier

object cpape_test extends App{
  
  val pubkey_l = "files/pub_key"
  val masterkey_l = "files/master_key"
    
  val privkey_l = "files/private_key"
  
  val cp = new DirectCPABE
  
  //cp.setup(pubkey_l,masterkey_l)
  
  //cp.keygen(pubkey_l,privkey_l,masterkey_l,"pri:4 role:admin host:poney")
  val policy = "poney pri<=5 2of2 admin 1of2"
  val encdat = cp.enc(pubkey_l, policy, "Poney".getBytes)
  
  val b64 = BASE64.enc(encdat)
  println(b64)
  
  val d64 = BASE64.dec(b64)

  val orig = cp.dec(pubkey_l, privkey_l,d64)
  println(Stringifier(orig))
  
}