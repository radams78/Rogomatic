val scala3Version = "3.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "RogOMatic",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    resolvers += Resolver.jcenterRepo,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    libraryDependencies += "org.jetbrains.pty4j" % "pty4j" % "0.12.7",
    libraryDependencies += "org.jetbrains.pty4j" % "purejavacomm" % "0.0.11.1"
  )
