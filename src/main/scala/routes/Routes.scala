package routes

import cats.effect.IO
import domain.{PricePoint, PriceStats}
import org.http4s.HttpRoutes
import org.http4s.Method.POST
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.dsl.io.*
import org.http4s.dsl.request.Root
import service.PriceService

class Routes(service: PriceService) {
  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> Root / "prices" / "ingest" =>
      for {
        pricePoint <- req.as[PricePoint]
        _ <- service.recordPrice(pricePoint)
        resp <- Created(s"Получена запись о продукте id${pricePoint.productId}")
      } yield resp
    case req@POST -> Root / "prices" / "batch_ingest" =>
      for {
        points <- req.as[List[PricePoint]]
        _ <- service.recordPriceBatch(points)
        resp <- Created(s"Получено записей о продуктах: ${points.size}")
      } yield resp
    case req@GET -> Root / "analytics" / "get_price_stats" / productId =>
      for {
        result <- service.getPriceStats(productId)
        resp <- result match {
          case Some(stats) => Ok(stats)
          case None => NotFound(s"Статистика цен для товара с id: $productId не найдена")
        }
      } yield resp
  }
}
