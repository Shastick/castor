package decoder.reader
import util.messages.SyslogMsg
import scala.actors.Actor
import digester.LogHandler

/**
 * Dump trait representing anything able to read log data.
 */
abstract class LogReader(dest: LogHandler) extends Actor{

  /**
   * Read the log data and send it to the first LogHandler
   */
  def beamLogData()
  
  def act() = beamLogData
  
}