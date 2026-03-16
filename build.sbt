ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.8.2"

val http4sVersion = "0.23.26"
val circeVersion  = "0.14.6"
val doobieVersion = "1.0.0-RC5"

lazy val root = (project in file("."))
  .settings(
    name := "price-tracker",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect"         % "3.5.4",
      "org.http4s"    %% "http4s-ember-server" % http4sVersion,
      "org.http4s"    %% "http4s-dsl"          % http4sVersion,
      "org.http4s"    %% "http4s-circe"        % http4sVersion,
      "io.circe"      %% "circe-generic"       % circeVersion,
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari"   % doobieVersion,
      "ch.qos.logback" % "logback-classic" % "1.5.32",
      "org.flywaydb" % "flyway-core" % "12.1.0",
      "org.flywaydb" % "flyway-database-postgresql" % "12.1.0"
    )
  )
