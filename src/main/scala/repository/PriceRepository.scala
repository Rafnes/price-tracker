package repository

import cats.effect.IO
import domain.PricePoint
import doobie.Update
import doobie.implicits.toSqlInterpolator
import doobie.syntax.all.toConnectionIOOps
import doobie.util.transactor.Transactor

class PriceRepository(xa: Transactor[IO]) {
  def save(pricePoint: PricePoint): IO[Int] = {
    sql"""
         INSERT INTO prices (product_id, product_name, store_name, price)
         VALUES (${pricePoint.productId}, ${pricePoint.productName}, ${pricePoint.storeName}, ${pricePoint.price})
         """.update.run.transact(xa)
  }

  def saveBatch(pricePoints: List[PricePoint]) : IO[Int] = {
    val sqlStatement = "INSERT INTO prices (product_id, product_name, store_name, price) VALUES (?, ?, ?, ?)"
    Update[PricePoint](sqlStatement).updateMany(pricePoints).transact(xa)
  }
}
