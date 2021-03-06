name := """ApacheCMDA_Backend"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.4"

jacoco.settings

libraryDependencies ++= Seq(
  cache,
  javaWs,
  javaCore,
  "org.springframework" % "spring-context" % "4.1.4.RELEASE",
  "javax.inject" % "javax.inject" % "1",
  "org.springframework.data" % "spring-data-jpa" % "1.7.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.1.4.RELEASE",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.7.Final",
  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.hibernate" % "hibernate-c3p0" % "4.3.7.Final",
  "org.apache.lucene" % "lucene-analyzers-common" % "5.3.1",
  "org.apache.lucene" % "lucene-core" % "5.3.1",
  "org.apache.lucene" % "lucene-queryparser" % "5.3.1",
  "org.apache.spark" % "spark-core_2.11" % "1.5.1",
  "org.apache.spark" % "spark-mllib_2.11" % "1.5.1"
)


