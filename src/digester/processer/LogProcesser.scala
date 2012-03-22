package digester.processer


trait LogProcesser {
  def crunchLine(in: Array[Byte]):Array[Byte]
  def crunchDGram(in: ClrSyslogMsg):
}