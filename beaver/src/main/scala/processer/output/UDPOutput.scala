package processer.output
import processer.Handler
import util.messages.SyslogMsg
import util.messages.AdminMsg
import java.net.DatagramSocket
import java.net.DatagramPacket
import util.messages.CipherSyslogMsg
import java.net.InetAddress
import util.messages.FullCipherText
import util.messages.ClearSyslogMsg
import java.text.SimpleDateFormat
import java.util.Date

class UDPOutput(socket: DatagramSocket, dest:  InetAddress, port: Int) extends Handler {

	/**
	 * priority for admin messages
	 * set to 4 ("security/authorization messages")
	 * see http://www.ietf.org/rfc/rfc3164.txt
	 * for details
	 */
	val pri = 4
	val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	
	def this(dest: String, port: Int) =
	  this(new DatagramSocket, InetAddress.getByName(dest), port)
	  
	def procDgram(dg: SyslogMsg) = dg match {
	  case m: CipherSyslogMsg => send(m.toBytes)
	  case m: ClearSyslogMsg => send(m.toBytes)
	  case m: FullCipherText => //TODO
	}
	
	def procAdminMsg(m: AdminMsg) = send( 
	  new ClearSyslogMsg(
	      pri.toString(),
	      dateFormat.format(new Date()),
	      InetAddress.getLocalHost().getHostName(),
	      m.toString)
	  toBytes
	  )
	
	private def send(b: Array[Byte]) = 
	  socket.send(new DatagramPacket(b, b.length, dest, port))
	  
}