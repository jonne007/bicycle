package backend

import utest._

object BicycleSuite extends TestSuite {

  val tests = Tests {
    test("test get") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("cresent", 202.2, 44)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val ab = bb.addBicycle(bc2)

      val r = bb.get(ab).get

      r ==> bc2

    }
    test("test addBicycle") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("Cresent", 1112.3, 11)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val ab = bb.addBicycle(bc2)

      val r = bb.list().size

      r ==> 1
    }
    test("test list") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("Cresent", 1112.3, 11)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)
      val adbc3 = bb.addBicycle(bc3)

      val r = bb.list()

      r.size ==> 3
    }
    test("test update") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("Cresent", 1112.3, 11)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val adbc = bb.addBicycle(bc1)
      val gbc = bb.get(adbc).get.copy(stock = 33)

      bb.update(adbc, gbc)

      val r = bb.get(adbc).get.stock

      r ==> 33
    }
    test("test buy") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("Cresent", 1112.3, 11)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val adbc = bb.addBicycle(bc1)

      val cost = bb.buy(adbc, 2)

      val r = cost

      r ==> 2224.6
    }
    test("test delete") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("Cresent", 1112.3, 11)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)

      bb.delete(adbc1)

      val r = bb.list()

      r.size ==> 1
    }
    test("test searchByBrand") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("Cresent", 1112.3, 11)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val bc4 = Bicycle("cresent", 250.50, 7)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)
      val adbc3 = bb.addBicycle(bc3)
      val adbc4 = bb.addBicycle(bc4)

      val r = bb.searchByBrand("cresent")

      r.size ==> 2
    }
    test("test searchByMaxPrice") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("Cresent", 1112.3, 11)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val bc4 = Bicycle("cresent", 250.50, 7)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)
      val adbc3 = bb.addBicycle(bc3)
      val adbc4 = bb.addBicycle(bc4)

      val r = bb.searchByMaxPrice(500)

      r.size ==> 3

    }
    test("test searchByBrandnPrice") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("Cresent", 1112.3, 11)
      val bc2 = Bicycle("monark", 100.5, 22)
      val bc3 = Bicycle("canyon", 500, 10)
      val bc4 = Bicycle("cresent", 250.50, 7)
      val bc5 = Bicycle("cresent", 434, 7)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)
      val adbc3 = bb.addBicycle(bc3)
      val adbc4 = bb.addBicycle(bc4)
      val adbc5 = bb.addBicycle(bc5)

      val r = bb.searchByBrandnPrice(434, "cresent")

      r.size ==> 2

    }
  }
}
