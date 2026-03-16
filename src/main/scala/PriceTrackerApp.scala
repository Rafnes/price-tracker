import cats.effect.{IO, IOApp}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import repository.PriceRepository
import config.Database
import routes.Routes
import service.PriceService
import com.comcast.ip4s._


object PriceTrackerApp extends IOApp.Simple {
  override def run: IO[Unit] = {
    val dbUrl = "jdbc:postgresql://localhost:5432/price_tracker"
    val dbUser = "price_tracker_admin"
    val dbPass = "price_tracker_pass"

    for {
      _ <- IO.println("Запуск миграций...")
      _ <- Database.runMigrations(dbUrl, dbUser, dbPass)
      _ <- IO.println("Запуск сервера...")
      _ <- Database.transactor.use { xa =>
        val priceRepository = new PriceRepository(xa)
        val priceService = new PriceService(priceRepository)
        val routes = new Routes(priceService)

        val httpApp = Router("api" -> routes.routes).orNotFound

        EmberServerBuilder.default[IO]
          .withHost(host"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(httpApp)
          .build
          .useForever
      }
    } yield ()
  }
}
