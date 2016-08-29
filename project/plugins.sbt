logLevel := Level.Warn
// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.2")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")

// Play enhancer - this automatically generates getters/setters for public fields
// and rewrites accessors of these fields to use the getters/setters. Remove this
// plugin if you prefer not to have this feature, or disable on a per project
// basis using disablePlugins(PlayEnhancer) in your build.sbt
addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.1.0")

// Play Ebean support, to enable, uncomment this line, and enable in your build.sbt using
// enablePlugins(SbtEbean). Note, uncommenting this line will automatically bring in
// Play enhancer, regardless of whether the line above is comme
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "1.0.0")

addSbtPlugin("com.github.play2war" % "play2-war-plugin" % "1.4-beta1")
//FOR PLAY 2.3
/*

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.github.play2war" % "play2-war-plugin" % "1.3-beta3")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.8")
*/


// https://github.com/orrsella/sbt-stats
// Para generar un reporte de archivos, su tipo y su peso
// activator stats
// los reportes se encuentra en la consola
addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5")
// https://github.com/sbt/findbugs4sbt
// Genera un reporte donde se ven vulnerabilidades, bugs y otra
// informacion para auditar el codigo de cada quien
// activator findbugs
// los reportes de encuentran en targe/findbugs
addSbtPlugin("de.johoop" % "findbugs4sbt" % "1.4.0")
// https://github.com/sbt/cpd4sbt
// Genera un reporte notificando lugares donde el codigo fue copiado tal cual,
// si se detecta hay que quitarlos haciendo un metodo compartido para ambos lugares
// activator cpd
//los reportes de encuentran en target/cpd
addSbtPlugin("de.johoop" % "cpd4sbt" % "1.2.0")