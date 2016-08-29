import com.github.play2war.plugin.{Play2WarKeys, Play2WarPlugin}

name := "PlayAngularBase"
version := "1.0"
scriptClasspath := Seq("*")
scalaVersion := "2.11.6"

Play2WarPlugin.play2WarSettings
Play2WarKeys.servletVersion := "3.1"
Play2WarKeys.targetName := Some("PlayAngularBase")
Play2WarKeys.explodedJar := true

findbugsSettings
findbugsReportType := Some(de.johoop.findbugs4sbt.ReportType.FancyHistHtml)
findbugsReportPath := Some(crossTarget.value / "findbugs" / "findbugs.html")
findbugsExcludeFilters := Some(scala.xml.XML.loadFile(baseDirectory.value / "project" / "findbugs-exclude-filters.xml"))

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies ++= Seq(
//App Dependencies
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "be.objectify" %% "deadbolt-java" % "2.4.3",
  "javax.mail" % "mail" % "1.4",
  "org.postgresql" % "postgresql" % "9.4-1202-jdbc42",
// Web Client Depedencies
  "org.webjars" % "requirejs" % "2.1.22",
  "org.webjars" % "angularjs" % "1.5.5",
  "org.webjars.bower" % "angular-material" % "1.1.0-rc4",
  "org.webjars.bower" % "angular-material-icons" % "0.7.0",
  "org.webjars.bower" % "angular-material-data-table" % "0.10.9",
  "org.webjars" % "angular-ui-router" % "0.2.15",
  "org.webjars.bower" % "ngstorage" % "0.3.10",
  "org.webjars" % "angular-translate" % "2.8.1",
  "org.webjars" % "angular-translate-loader-static-files" % "2.6.1-1",
  "org.webjars" % "angular-translate-handler-log" % "2.4.2",
  "org.webjars.bower" % "angular-underscore-module" % "1.0.3"
)     

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := StaticRoutesGenerator


// For play 2.3, replace all with
/*
import play.PlayJava
import com.github.play2war.plugin._
import com.typesafe.sbt.packager.Keys._


name := "Launcher"

version := "1.0"

scriptClasspath := Seq("*")

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "2.5"

Play2WarKeys.targetName := Some("launcher")

Play2WarKeys.explodedJar := true

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaEbean,
  javaWs,
//App Dependencies
  "org.webjars" % "webjars-play_2.11" % "2.3.0-3",
  "be.objectify" % "deadbolt-java_2.11" % "2.3.3",
  "javax.mail" % "mail" % "1.4",
  "net.sourceforge.jtds" % "jtds" % "1.2",
// Web Client Depedencies
  "org.webjars" % "requirejs" % "2.1.22",
  "org.webjars" % "angularjs" % "1.4.8",
  "org.webjars" % "angular-material" % "1.0.1",
  "org.webjars.bower" % "angular-material-icons" % "0.6.0",
  "org.webjars.bower" % "angular-material-data-table" % "0.10.3",
  "org.webjars" % "angular-ui-router" % "0.2.15",
//  "org.webjars.bower" % "a0-angular-storage" % "0.0.13",
  "org.webjars.bower" % "ngstorage" % "0.3.10",
  "org.webjars" % "angular-translate" % "2.8.1",
  "org.webjars" % "angular-translate-loader-static-files" % "2.6.1-1",
  "org.webjars" % "angular-translate-handler-log" % "2.4.2",
  "org.webjars.bower" % "angular-underscore-module" % "1.0.3",
  "org.webjars" % "nervgh-angular-file-upload" % "2.1.1"
)
 */