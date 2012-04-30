package processer.input
import java.net.DatagramSocket
import java.net.DatagramPacket
import util.Stringifier
import processer.Processer
import processer.Handler
import util.messages.Parser

class UDPInput(socket: DatagramSocket
				,processer: Handler) extends LogInput(processer){
  
	val packet_length = 2048
	def this(port: Int, proc: Processer) =
	  this(new DatagramSocket(port),proc)
	
	def beamLogData() =  loop {
		processer ! Parser.fromInput(readDatagram)
	}
	
	def readLine():String = readDatagram()
	
	def readDatagram(): String = {
	 val buff = new Array[Byte](packet_length)
	 val p = new DatagramPacket(buff,packet_length)
	 socket.receive(p)
	 Stringifier(p.getData())
	}
}