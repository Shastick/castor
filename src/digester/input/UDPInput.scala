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
				,processer: LogProcesser
				,writer: LineWriter) extends MsgInput{
  
	val packet_length = 2048
	def this(port: Int, proc: LogProcesser,writ: LineWriter) =
	  this(new DatagramSocket(port),proc,writ)
	
	def act() = while(true) 
		writer.writeDgram(processer.crunchDGram(SyslogParser.parseClearText(readDatagram)))
	  	
	
	def readLine():String = readDatagram()
	
	def readDatagram():String = {
	 val buff = new Array[Byte](packet_length)
	 val p = new DatagramPacket(buff,packet_length)
	 socket.receive(p)
	 // Yes, this is ugly.
	 // And yes, it is done on purpose, as if I want a string I can use in regexp's,
	 // just calling new String(p.getData()) won't cut it... >.>
	 // TODO @julien try to find a better way around, like not creating new readers each time
	 // or checking if a String contructor is available with options like new String(p.getData(), "UTF8") 
	 Stringifier(p.getData())
	}
}