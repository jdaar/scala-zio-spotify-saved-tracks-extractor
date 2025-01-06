ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.3"

lazy val versions = Map(
  "zio" -> "2.1.14",
  "zio-streams" -> "2.1.14",
  "zio-http" -> "3.0.1",
  "zio-config" -> "4.0.3",
  "zio-json" -> "0.7.3",
  "zio-schema" -> "1.5.0"
)

lazy val root = (project in file("."))
  .settings(
    name := "spotifyexporter",
    idePackagePrefix := Some("dev.jhonatan"),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % versions("zio"),
      "dev.zio" %% "zio-streams" % versions("zio-streams"),
      "dev.zio" %% "zio-http" % versions("zio-http"),
      "dev.zio" %% "zio-config" % versions("zio-config"),
      "dev.zio" %% "zio-config-refined" % versions("zio-config"),
      "dev.zio" %% "zio-config-typesafe" % versions("zio-config"),
      "dev.zio" %% "zio-json" % versions("zio-json"),
      "dev.zio" %% "zio-schema" % versions("zio-schema"),
      "dev.zio" %% "zio-schema-json" % versions("zio-schema")
    )
  )
