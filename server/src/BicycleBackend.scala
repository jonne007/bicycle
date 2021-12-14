package backend

import java.util.UUID
import upickle.default.{ReadWriter => RW, macroRW}

case class Bicycle(Id: BicycleId, brand: String, price: Double, stock: Int)
object Bicycle {
  implicit val rw: RW[Bicycle] = macroRW
}

type BicycleId = String

trait BicycleBackend {

  def createID() = UUID.randomUUID.toString()
  def get(b: String): Option[Bicycle]
  def addBicycle(b: Bicycle): String
  def list(): List[Bicycle]
  def update(id: String, b: Bicycle): Unit
  def searchByBrand(b: String): List[Bicycle]
  def searchByMaxPrice(p: Double): List[Bicycle]
  def searchByBrandnPrice(p: Double, b: String): List[Bicycle]
  def buy(b: String, amount: Int): Double
  def delete(b: String): Unit
}
class InMemoryBicycleBackend extends BicycleBackend {
  var cycles: Map[String, Bicycle] = Map.empty

  def get(id: String): Option[Bicycle] = {
    cycles.get(id)
  }
  def addBicycle(b: Bicycle): String = {
    val id = createID()
    cycles = cycles + (id -> b)
    id

  }
  def list(): List[Bicycle] = cycles.values.toList
  def update(id: String, b: Bicycle): Unit = {
    cycles = cycles + (id -> b)

  }
  def searchByBrand(b: String): List[Bicycle] = search { c =>
    c.brand
      .toLowerCase()
      .contains(b.toLowerCase())

  }
  def searchByMaxPrice(p: Double): List[Bicycle] = {
    search(c => c.price <= p)

  }

  def searchByBrandnPrice(p: Double, b: String): List[Bicycle] = {
    val cycles2 = list().filter(c => c.price <= p)
    cycles2.filter(c =>
      c.brand
        .toLowerCase()
        .contains(b.toLowerCase())
    )

  }
  def buy(b: String, amount: Int): Double = {

    val c = get(b).get
    val newAmount = c.stock - amount
    update(b, c.copy(stock = newAmount))
    c.price * amount

  }
  def delete(b: String): Unit = {
    val bc1 = cycles.removed(b)
    cycles = bc1
  }

  def search(b: Bicycle => Boolean): List[Bicycle] = list().filter(b)
}
