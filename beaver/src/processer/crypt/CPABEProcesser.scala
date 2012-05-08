package processer.crypt

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.BufferedReader
import cpabe.Cpabe
import cpabe.AESCoder
import bswabe.SerializeUtils
import cpabe.Common
import bswabe.Bswabe
import java.io.ByteArrayInputStream
import processer.Processer
import processer.Handler
import util.cipher.DirectCPABE
import util.messages.SyslogMsg
import util.messages.ClearSyslogMsg
import util.messages.CipherSyslogMsg
import util.messages.FullCipherText
import util.messages.Parser
import util.Stringifier
import util.messages.UnauthorizedAccess

class CPABEProcesserEnc(next: Handler, c: DirectCPABE) extends Processer(next){
	/**
	 * Alternate constructor
	 */
  def this(next: Handler, pubkey_loc: String) = this(next, new DirectCPABE(pubkey_loc))
  
  override def crunchDgram(m: SyslogMsg): SyslogMsg = m match {
    case m: ClearSyslogMsg =>
      val policy = "host:" + m.host + " pri:" + m.pri + " 2of2 role:admin 1of2"
      new FullCipherText(c.enc(policy,m.toBytes))
    case m: CipherSyslogMsg =>
      throw new Exception("Encryption CPABE Processor does not support Ciphertext inputs.")
  }
  
  def crunchArray(in: Array[Byte]) = 
    throw new Exception("CPABEProcessors directly override crunchDgram " +
    		"and do not need crunchArray")
}

class CPABEProcesserDec(next: Handler, c: DirectCPABE, pk: Array[Byte]) extends Processer(next){
  /**
   * Alternate constructor
   */
  def this(next: Handler, pubkey_loc: String, privkey_loc: String) =
    this(next, new DirectCPABE(pubkey_loc), Common.suckFile(privkey_loc))
  
  override def crunchDgram(m: SyslogMsg): SyslogMsg = m match {
    case m: FullCipherText => tryDecryption(m.p)
  }
  
  private def tryDecryption(data: Array[Byte]): SyslogMsg = {
    val d = c.dec(pk,data)
    if(d.isDefined) Parser.fromInput(Stringifier(d.get))
    else UnauthorizedAccess
  }
  
  def crunchArray(in: Array[Byte]) = 
	throw new Exception("CPABEProcessors directly override crunchDgram " +
    		"and do not need crunchArray")
  
}