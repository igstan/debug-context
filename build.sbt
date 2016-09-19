val main = project in file("modules/main") settings(
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  scalacOptions ++= Seq(
    "-feature",
    "-unchecked",
    "-deprecation",
    "-language:experimental.macros",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-Xlint:_",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  )
)

val test = project in file("modules/test") dependsOn main settings(
  name := "debug-context",
  organization := "com.eloquentix",
  version := "0.1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  ),
  scalacOptions ++= Seq(
    "-feature",
    "-unchecked",
    "-deprecation",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-Xlint:_",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  )
)

val root = project in file(".") aggregate(main, test)
