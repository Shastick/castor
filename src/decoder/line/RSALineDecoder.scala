package decoder.line
import decoder.reader.LogReader

class RSALineDecoder(lines: LogReader) extends LineDecoder(lines) {

  def extractLine(): String = {
    "LOL"
  }
}