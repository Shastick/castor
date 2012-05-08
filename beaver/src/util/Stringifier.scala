package util
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

/**
 * Stringifier does all the String <-> Array[Byte] conversion.
 * As i'm not yet quite sure it is done correctly, I prefer keeping it in one place
 * if later corrections are required.
 */
object Stringifier {
  
  def apply(in: String): Array[Byte] = toBytes(in)
  
  def toBytes(in: String): Array[Byte] = {
    in.getBytes("UTF-8")
  }
  
  def apply(in: Array[Byte]): String = makeString(in)
  
  def makeString(in: Array[Byte]): String = {
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