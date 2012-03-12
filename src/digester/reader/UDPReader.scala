package digester.reader
import java.net.DatagramSocket
import java.net.DatagramPacket
import digester.processer.LogProcesser
import digester.writer.LineWriter

class UDPReader(socket: DatagramSocket
				,processer: LogProcesser
				,writer: LineWriter) extends Reader{
  
	val packet_length = 1024
	def this(port: Int, proc: LogProcesser,writ: LineWriter) =
	  this(new DatagramSocket(port),proc,writ)
	
	def act() {
	  while(true){
		  writer.writeLine(processer.crunchLine(readPacket))
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
	 p.getData()
	}
}