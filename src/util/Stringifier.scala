package util
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ByteArrayInputStream

object Stringifier {
  
  def apply(in: Array[Byte]):String = {
	val string_stream = new BufferedReader(
			 new InputStreamReader(
			     new ByteArrayInputStream(in)))
	 string_stream.readLine()
  }
}