package digester
import scala.actors.IScheduler
import digester.auth.Hasher
import scala.actors.Actor
import util.messages.SaveState
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