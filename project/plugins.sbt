// Sbt eclipse plugin
resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.0.0")

// Add a repo
resolvers += "twitter-repo" at "http://maven.twttr.com"

libraryDependencies ++= Seq("com.twitter" % "util-core" % "1.9.2","com.twitter" % "util-eval" % "1.12.13")
