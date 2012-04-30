package digester.input
import java.net.DatagramSocket
import java.net.DatagramPacket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ByteArrayInputStream
import util.Stringifier
import digester.LogHandler
import digester.LogProcesser
import util.messages.Parser
import decoder.reader.LogInput

class UDPInput(socket: DatagramSocket
				,processer: LogHandler) extends LogInput(processer){
  
	val packet_length = 2048
	def this(port: Int, proc: LogProcesser) =
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