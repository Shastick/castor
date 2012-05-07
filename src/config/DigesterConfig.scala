package config
import processer.crypt.AESProcesser
import processer.Handler

class AESConfig extends com.twitter.util.Config[AESProcesser] {
	var next = required[Handler]
	var keystore = required[String]
	var kAlias = required[String]
	var mode = required[String]
	
	def apply(): AESProcesser = 
}