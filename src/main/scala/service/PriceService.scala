package service

import cats.effect.IO
import domain.{PricePoint, PriceStats}
import repository.PriceRepository

class PriceService(repo: PriceRepository) {

  def recordPrice(pp: PricePoint): IO[Unit] =
    validatePricePoint(pp) match {
      case Left(error) =>
        IO.println(s"Отказ в записи: $error")
      case Right(valid) =>
        for {
          _ <- repo.save(valid)
          _ <- IO.println(s"Успешная запись: [${valid.storeName}] ${valid.productId}")
        } yield ()
    }

  def recordPriceBatch(points: List[PricePoint]): IO[String] = {
    val (errors, validPoints) = points.map(validatePricePoint).partitionMap(identity)

    for {
      _ <- if (errors.nonEmpty) IO.println(s"Пропущено ошибочных записей: ${errors.size}")
      else IO.unit

      _ <- if (validPoints.nonEmpty) repo.saveBatch(validPoints) else IO.pure(0)

      _ <- IO.println(s"Сохранено валидных записей: ${validPoints.size}")
    } yield s"Обработано ${points.size}, успешно: ${validPoints.size}, ошибок: ${errors.size}"
  }

  private def validatePricePoint(pp: PricePoint): Either[String, PricePoint] = {
    if (pp.price <= 0) Left(s"Ошибка валидации: цена меньше или равна 0: id продукта: ${pp.productId}, цена: ${pp.price}, магазин ${pp.storeName}")
    else Right(pp)
  }

  def getPriceStats(productId: String): IO[Option[PriceStats]] = {
    for {
      result <- repo.getStats(productId)
      _ <- result match {
        case Some(stats) => IO.println(s"Получена статистика по ценам товар: id: $productId")
        case None => IO.println(s"Статистика для продукта с id: $productId не найдена")
      }
    } yield result
  }
}