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
     
    /** Yes, this looks ugly.
	   * And yes, it is done on purpose, as if I want a string I can use in regexp's,
	   * just calling new String(p.getData()) won't cut it... >.>
	   * Currently works, so it will stay like this if it doesn't become an issue.
	   */
	  
	val bais = new ByteArrayInputStream(in)
    val sStream = new BufferedReader(
			 new InputStreamReader(
			     new ByteArrayInputStream(in)))
	sStream.readLine()
  }
}