package util
import scala.actors.Actor
import scala.actors.TIMEOUT

object ScheduleManager {
  def scheduler(time: Int)(f: => Unit) = {
	def fixedRateLoop {
	  Actor.reactWithin(time.toLong * 1000) {
	      case TIMEOUT => f; fixedRateLoop
	      case 'stop => 
	    }
	  }
	  Actor.actor(fixedRateLoop)
	}
}