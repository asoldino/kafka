name := "kafka"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.17",

  "ch.qos.logback" % "logback-classic" % "1.1.7",

  "com.typesafe.akka" %% "akka-http-core" % "10.0.4",
  "com.typesafe.akka" %% "akka-http" % "10.0.4",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.4",

  "com.typesafe.akka" %% "akka-stream-kafka" % "0.13"
)