package backend

import java.util.UUID
import upickle.default.{ReadWriter => RW, macroRW}

case class Bicycle(id: BicycleId, brand: String, price: Double, stock: Int)
object Bicycle {
  implicit val rw: RW[Bicycle] = macroRW
}

type BicycleId = String

trait BicycleBackend {

  def createID() = UUID.randomUUID.toString()
  def get(bid: BicycleId): Option[Bicycle]
  def addBicycle(b: Bicycle): BicycleId
  def list(): List[Bicycle]
  def update(bid: BicycleId, b: Bicycle): Unit
  def searchByBrand(brand: String): List[Bicycle]
  def searchByMaxPrice(price: Double): List[Bicycle]
  def searchByBrandnPrice(price: Double, brand: String): List[Bicycle]
  def buy(bid: BicycleId, amount: Int): Double
  def delete(bid: BicycleId): Unit
}
class InMemoryBicycleBackend extends BicycleBackend {
  var cycles: Map[String, Bicycle] = Map.empty

  def get(bid: BicycleId): Option[Bicycle] = {
    cycles.get(bid)
  }
  def addBicycle(b: Bicycle): BicycleId = {
    val bid = createID()
    cycles = cycles + (bid -> b.copy(id = bid))
    bid

  }
  def list(): List[Bicycle] = cycles.values.toList
  def update(bid: BicycleId, b: Bicycle): Unit = {
    cycles = cycles + (bid -> b)

  }
  def searchByBrand(brand: String): List[Bicycle] = search { c =>
    c.brand
      .toLowerCase()
      .contains(brand.toLowerCase())

  }
  def searchByMaxPrice(price: Double): List[Bicycle] = {
    search(c => c.price <= price)

  }

  def searchByBrandnPrice(price: Double, brand: String): List[Bicycle] = {
    val cycles2 = list().filter(c => c.price <= price)
    cycles2.filter(c =>
      c.brand
        .toLowerCase()
        .contains(brand.toLowerCase())
    )

  }
  def buy(bid: BicycleId, amount: Int): Double = {

    val c = get(bid).get
    val newAmount = c.stock - amount
    update(bid, c.copy(stock = newAmount))
    c.price * amount

  }
  def delete(bid: BicycleId): Unit = {
    val bc1 = cycles.removed(bid)
    cycles = bc1
  }

  def search(b: Bicycle => Boolean): List[Bicycle] = list().filter(b)
}
