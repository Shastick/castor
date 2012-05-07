import sbt._
import sbt.Defaults._ 
import Keys._



object LogBuild extends Build {

  scalaVersion := "2.9.0-1"

  lazy val liftVersion = "2.4"

  
  lazy val DigesterSettings = Seq(
	libraryDependencies ++= Seq(
	  "com.twitter" % "util-core" % "1.9.2",
	  "com.twitter" % "ostrich" % "2.3.0",
	  "com.twitter" % "xrayspecs_2.8.0" % "2.1.1",
	  "org.scala-tools" % "vscaladoc" % "1.2-m1",
	  "org.bouncycastle" % "bcprov-jdk16" % "1.46"
	))

  lazy val digester = Project(
    id = "log_digester",
    base = file(".")
  ) settings(DigesterSettings :_*)

}
