package backend

import utest._

object BicycleSuite extends TestSuite {

  val tests = Tests {
    test("test get") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "cresent", 202.2, 44)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      
      val ab = bb.addBicycle(bc2)

      val r = bb.get(ab).get

      r.id ==> ab

    }
    test("test addBicycle") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "Cresent", 1112.3, 11)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      val bc3 = Bicycle("id3", "canyon", 500, 10)
      val ab = bb.addBicycle(bc2)
      val ab2 = bb.addBicycle(bc1)
      val r = bb.list().size
    }
    test ("list") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "Cresent", 1112.3, 11)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      val bc3 = Bicycle("id3", "canyon", 500, 10)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)
      val adbc3 = bb.addBicycle(bc3)

      val r = bb.list()

      r.size ==> 6
    }
    test("test update") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "Cresent", 1112.3, 11)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      val bc3 = Bicycle("id3", "canyon", 500, 10)
      val adbc = bb.addBicycle(bc1)
      val gbc = bb.get(adbc).get.copy(stock = 33)

      bb.update(adbc, gbc)

      val r = bb.get(adbc).get.stock

      r ==> 33
    }
    test("test buy") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "Cresent", 1112.3, 11)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      val bc3 = Bicycle("id3", "canyon", 500, 10)
      val adbc = bb.addBicycle(bc1)

      val cost = bb.buy(adbc, 2)
      val updated = bb.get(adbc).get

      val r = cost

      r ==> 2224.6
      updated.stock ==> 9 
    }
    test("test delete") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "Cresent", 1112.3, 11)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      val bc3 = Bicycle("id3", "canyon", 500, 10)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)

      bb.delete(adbc1)

      val r = bb.list()

      r.size ==> 4
    }
    test("test searchByBrand") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "Cresent", 1112.3, 11)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      val bc3 = Bicycle("id3", "canyon", 500, 10)
      val bc4 = Bicycle("id4", "cresent", 250.50, 7)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)
      val adbc3 = bb.addBicycle(bc3)
      val adbc4 = bb.addBicycle(bc4)

      val r = bb.searchByBrand("cresent")

      r.size ==> 3
    }
    test("test searchByMaxPrice") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "Cresent", 1112.3, 11)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      val bc3 = Bicycle("id3", "canyon", 500, 10)
      val bc4 = Bicycle("id4", "cresent", 250.50, 7)
      val adbc1 = bb.addBicycle(bc1)
      val adbc2 = bb.addBicycle(bc2)
      val adbc3 = bb.addBicycle(bc3)
      val adbc4 = bb.addBicycle(bc4)

      val r = bb.searchByMaxPrice(500)

      r.size ==> 5

    }
    test("test searchByBrandnPrice") - {
      val bb = new InMemoryBicycleBackend()

      val bc1 = Bicycle("id1", "Cresent", 1112.3, 11)
      val bc2 = Bicycle("id2", "monark", 100.5, 22)
      val bc3 = Bicycle("id3", "canyon", 500, 10)
      val bc4 = Bicycle("id4", "cresent", 250.50, 7)
      val bc5 = Bicycle("id5", "cresent", 434, 7)
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
