organization := "com.github.krasserm"

name := "akka-persistence-kafka"

version := "0.5-SNAPSHOT"

scalaVersion := "2.11.6"

scalacOptions += "-target:jvm-1.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

resolvers += "krasserm at bintray" at "http://dl.bintray.com/krasserm/maven"

//resolvers += Resolver.mavenLocal

publishTo := Some(Resolver.mavenLocal)

parallelExecution in Test := false

publishArtifact in Test := true

val kafkaDataDirectory = settingKey[File]("The directory to store Kafka data during tests")

(kafkaDataDirectory in Test) := baseDirectory.value / "target" / "test"

testOptions in Test += Tests.Argument("-oF")

cancelable in Global := true

testOptions in Test += Tests.Setup{ () => 
  println("Deleting Kafka data directory before tests...")
  sbt.IO.delete((kafkaDataDirectory in Test).value)
  
  println("Deleting test logs tests...")
  
  val filter = new java.io.FileFilter {
    def accept(path: java.io.File): Boolean = {
      path.getName.endsWith(".log")
    }
  }
  
  val files = (baseDirectory in Test).value.listFiles(filter)
  
  //files.foreach(f => f.delete)
  
}
 

libraryDependencies ++= Seq(
  "com.google.protobuf"  %  "protobuf-java"        % "2.5.0",
  "com.typesafe.akka"    %% "akka-persistence"     % "2.4.6",
  "com.typesafe.akka"    %% "akka-slf4j"           % "2.4.6",
  "org.slf4j"            %  "log4j-over-slf4j"     % "1.7.5",
  "ch.qos.logback"       % "logback-classic"       % "1.1.2"   % "runtime",
  "com.typesafe.akka"    %% "akka-persistence-tck" % "2.4.6"   % Test,
  "commons-io"           %  "commons-io"           % "2.4"     % Test,
  "org.apache.kafka"     %  "kafka-clients"        % "0.9.0.0",
  "org.apache.zookeeper" %  "zookeeper"            % "3.4.6"   exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.kafka"     %% "kafka"                % "0.9.0.1" exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-log4j12"),  
  "org.apache.curator"   %  "curator-test"         % "2.7.1"   % Test,
  "org.apache.curator"   % "curator-client"        % "2.7.1"   % Test,
  "org.apache.curator"   % "curator-framework"     % "2.7.1"   % Test
  
)

EclipseKeys.withSource := true

