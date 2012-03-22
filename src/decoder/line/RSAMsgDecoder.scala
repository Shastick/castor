package decoder.line
import decoder.reader.LogReader

class RSAMsgDecoder(lines: LogReader) extends LineDecoder(lines) {

  def extractLine(): String = {
    "LOL"
  }
}