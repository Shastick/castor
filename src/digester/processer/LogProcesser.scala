package digester.processer


trait LogProcesser {
  def crunchLine(in: Array[Byte])
}