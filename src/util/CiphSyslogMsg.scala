package util


/**
 * CiphSyslogMsg represents a syslog message that has been processed 
 * and has some of its field potentially crypted, depending
 * on how the processer was configurated.
 * 
 * Syslog datagram format
 * (PRI)(TIMESTAMP\sHOSTNAME)\s(MSG)
 */

class CiphSyslogMsg(pri: String, header: SyslogHeader, msg: String)
	extends SyslogMsg(pri, header, msg)
