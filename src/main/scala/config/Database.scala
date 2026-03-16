package config

import cats.effect.{IO, Resource}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway

object Database {
  def transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      ec <- ExecutionContexts.fixedThreadPool[IO](16)
      xa <- HikariTransactor.newHikariTransactor[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/price_tracker",
        "price_tracker_admin",
        "price_tracker_pass",
        ec
      )
    } yield xa

  def runMigrations(url: String, user: String, pass: String): IO[Unit] = IO {
    val flyway = Flyway.configure()
      .dataSource(url, user, pass)
      .load()
    flyway.migrate()
  }.void
}
