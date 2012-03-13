package digester.reader
import java.net.DatagramSocket
import java.net.DatagramPacket
import digester.processer.LogProcesser
import digester.writer.LineWriter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ByteArrayInputStream


class UDPReader(socket: DatagramSocket
				,processer: LogProcesser
				,writer: LineWriter) extends MsgReader{
  
	val packet_length = 2048
	def this(port: Int, proc: LogProcesser,writ: LineWriter) =
	  this(new DatagramSocket(port),proc,writ)
	
	def act() {
	  while(true){
		  println(SyslogParser(readPacket))
	  }
	}
	
	def readLine():String = {
	  val bytes = readPacket
	  new String(bytes)
	}
	
	def readPacket():Array[Byte] = {
	 val buff = new Array[Byte](packet_length)
	 val p = new DatagramPacket(buff,packet_length)
	 socket.receive(p)
	 // Yes, this is ugly.
	 // And yes, it is done on purpose, as if I want a string I can use in regexp's,
	 // just calling new Strin(p.getData()) won't cut it... >.>
	 val string_stream = new BufferedReader(
			 new InputStreamReader(
			     new ByteArrayInputStream(p.getData())))
	 		
	 
	 p.getData()
	}
}