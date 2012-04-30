package processer.input
import scala.actors.Actor
import processer.Handler

/**
 * Dump trait representing anything able to read log data.
 */
abstract class LogInput(dest: Handler) extends Actor{

  /**
   * Read the log data and send it to the first Handler
   */
  def beamLogData()
  
  def act() = beamLogData
  
}