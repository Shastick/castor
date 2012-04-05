package digester.input
import java.net.DatagramSocket
import java.net.DatagramPacket
import digester.processer.LogProcesser
import digester.writer.LineWriter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ByteArrayInputStream
import util.SyslogParser
import util.Stringifier


class UDPInput(socket: DatagramSocket
				,processer: LogProcesser) extends MsgInput{
  
	val packet_length = 2048
	def this(port: Int, proc: LogProcesser) =
	  this(new DatagramSocket(port),proc)
	
	def act() = while(true) 
		processer.crunchDgram(SyslogParser.parseClearText(readDatagram))
	  	
	
	def readLine():String = readDatagram()
	
	def readDatagram():String = {
	 val buff = new Array[Byte](packet_length)
	 val p = new DatagramPacket(buff,packet_length)
	 socket.receive(p)
	 Stringifier(p.getData())
	}
}