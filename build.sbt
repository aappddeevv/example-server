name := "example-server"
organization := "example"
version := "0.1.0"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.url("file://" + Path.userHome.absolutePath + "/.ivy/local")
resolvers += Resolver.sonatypeRepo("releases")
resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "latest.release"
    ,"com.typesafe.akka" %% "akka-contrib" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-core" % "latest.release"
    ,"com.typesafe.akka" %% "akka-slf4j" % "latest.release"
    ,"com.typesafe.akka" %% "akka-stream" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-experimental" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-jackson-experimental" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-spray-json-experimental" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-xml-experimental" % "latest.release"
    ,"com.typesafe" % "config" % "latest.release"
    ,"org.scala-lang.modules" %% "scala-xml" % "latest.release"
    ,"com.beust" % "jcommander" % "latest.release"
    ,"ch.qos.logback" % "logback-classic" % "latest.release"
    ,"ch.qos.logback" % "logback-core" % "latest.release"
    ,"net.databinder.dispatch" %% "dispatch-core" % "latest.release"
    ,"com.typesafe.scala-logging" %% "scala-logging" % "latest.release"
    ,"org.scala-lang.modules" %% "scala-async" % "latest.release"
)

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.withSource := true

packSettings

packMain := Map("example-server" -> "example.Server")

