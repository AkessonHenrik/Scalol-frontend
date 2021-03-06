name := "Scalol-frontend"

version := "1.0"

scalaVersion := "2.12.2"


enablePlugins(ScalaJSPlugin)
name := "Scalol"
scalaVersion := "2.12.2" // or any other Scala version >= 2.10.2
scalaJSUseMainModuleInitializer := true
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2"
// This is an application with a main method
libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.1"

skip in packageJSDependencies := false
jsDependencies +=
"org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"

enablePlugins(ScalaJSPlugin)
libraryDependencies += "com.lihaoyi" %%% "utest" % "0.4.4" % "test"
testFrameworks += new TestFramework("utest.runner.Framework")
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.2"

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.3.0"

jsDependencies += RuntimeDOM