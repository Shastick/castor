package decoder

class RSALineDecoder(lines: LogReader) extends LineDecoder(lines) {

  def extractLine(): String
}