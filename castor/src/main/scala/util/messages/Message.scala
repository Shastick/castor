package util.messages
import util.BASE64

/**
 * Dummy Trait representing the messages exchanged between the different Actors.
 */
trait Message {
	lazy val conv = BASE64.getConverter
}