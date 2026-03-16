package repository

import cats.effect.IO
import domain.PricePoint
import doobie.implicits.toSqlInterpolator
import doobie.syntax.all.toConnectionIOOps
import doobie.util.transactor.Transactor

class PriceRepository(xa: Transactor[IO]) {
  def save(pricePoint: PricePoint): IO[Int] = {
    sql"""
         INSERT INTO prices (product_id, store_name, price)
         VALUES (${pricePoint.productId}, ${pricePoint.storeName}, ${pricePoint.price})
         """.update.run.transact(xa)
  }
}
