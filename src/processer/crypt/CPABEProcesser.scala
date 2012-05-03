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
import util.cipher.LogCipher
import processer.Handler

class DirectCPABE(next: Handler, c: LogCipher) extends Processer{

}