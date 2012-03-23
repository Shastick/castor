package util
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ByteArrayInputStream

object Stringifier {
  
  def apply(in: Array[Byte]):String = {
     // Yes, this is ugly.
	 // And yes, it is done on purpose, as if I want a string I can use in regexp's,
	 // just calling new String(p.getData()) won't cut it... >.>
	 // TODO @julien try to find a better way around, like not creating new readers each time
	 // or checking if a String contructor is available with options like new String(p.getData(), "UTF8") 
	val string_stream = new BufferedReader(
			 new InputStreamReader(
			     new ByteArrayInputStream(in)))
	 string_stream.readLine()
  }
}