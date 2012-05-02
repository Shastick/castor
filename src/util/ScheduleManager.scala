package util
import scala.actors.Actor
import scala.actors.TIMEOUT

object ScheduleManager {
  def scheduler(time: Long)(f: => Unit) = {
	def fixedRateLoop {
	  Actor.reactWithin(time) {
	      case TIMEOUT => f; fixedRateLoop
	      case 'stop => 
	    }
	  }
	  Actor.actor(fixedRateLoop)
	}
}