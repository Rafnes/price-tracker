package service

import cats.effect.IO
import domain.PricePoint
import repository.PriceRepository

class PriceService(repo: PriceRepository) {

  def recordPrice(pp: PricePoint): IO[Unit] = {
    if (pp.price <= 0) {
      IO.println(s"Ошибка записи: Цена меньше или равна 0:  цена: ${pp.price}, продукт: ${pp.productId}, магазин ${pp.storeName}")
    } else {
      for {
        _ <- repo.save(pp)
        _ <- IO.println(s"Успешная запись: [${pp.storeName}] ${pp.productId} -> ${pp.price}")
      } yield ()
    }
  }
}