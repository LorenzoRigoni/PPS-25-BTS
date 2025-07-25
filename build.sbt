ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .enablePlugins()
  .settings(
    name := "BTS",

    //scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0",

    mainClass := Some("Main"),
    assembly / assemblyJarName := "Brain-Training-Scala.jar",

    wartremoverErrors ++= Seq(
      Wart.Null,
      Wart.PublicInference
    ),

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.18" % Test,
      "junit" % "junit" % "4.13.2" % Test
    ),

    addCommandAlias("check", ";scalafmtCheckAll;compile;test")
  )

// Commands for QA testing

// sbt scalafmtCheckAll (Scalafmt tests)
// sbt compile (Wartremover tests)
// sbt scalafixAll --rules OrganizeImports (Scalafix tests)
// sbt test (Scalatest)