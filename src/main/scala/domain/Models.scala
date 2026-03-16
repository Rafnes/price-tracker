package domain

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class PricePoint(productId: String,
                      storeName: String,
                      price: Double)

object PricePoint {
  implicit val codec: Codec[PricePoint] = deriveCodec[PricePoint]
}

case class PriceStats(average: Double,
                      max: Double,
                      min: Double)

object PriceStats {
  implicit val codec: Codec[PriceStats] = deriveCodec[PriceStats]
}
