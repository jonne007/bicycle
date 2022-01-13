import io.undertow.Undertow
import utest._

object ServerSuite extends TestSuite {
  val tests = Tests {
    test("list") - withServer(WebServer) { host =>
      val r = requests.get(host + "/cycles")
      val json = ujson.read(r.text())

      val cycles = json("cycles").arr

      cycles.size ==> 0
    }
    test("add and list") - withServer(WebServer) { host =>
      val createResponse =
        requests.post(
          url = host + "/cycles",
          data = """{"id": "id1", "brand": "cresent", "price": 25, "stock": 12}"""
        )

      val json = ujson.read(createResponse.text())
      val id = json("bicycleId").str

      val r = requests.get(host + "/cycles")
      val json2 = ujson.read(r.text())

      val bicycles = json2("cycles").arr

      bicycles.size ==> 1
    }

    test("create and get") - withServer(WebServer) { host =>
      val createResponse =
        requests.post(
          url = host + "/cycles",
          data = """{"id": "id1", "brand": "cresent", "price": 25, "stock": 12}"""
        )

      val json = ujson.read(createResponse.text())
      val bid = json("bicycleId").str

      val r = requests.get(host + s"/cycles/$bid")
      val bicycleJson = ujson.read(r.text())

      bicycleJson(0)("stock").num ==> 12
    }

    test("create, get and update") - withServer(WebServer) { host =>
      val createResponse =
        requests.post(
          url = host + "/cycles",
          data =
            """{"id": "id1", "brand": "cresent", "price": 25, "stock": 12}"""
        )

      val json = ujson.read(createResponse.text())
      val bid = json("bicycleId").str

      val updateResponse =
        requests.post(
          url = host + s"/cycles/$bid/updateBrand",
          data = """{"brand": "monark"}"""
        )

      val updateJson = ujson.read(updateResponse.text())
      val updatedBrand = updateJson("updated")

      val getResponse = requests.get(host + s"/cycles/$bid")
      val bicycleJson = ujson.read(getResponse.text())

      val brand = bicycleJson(0)("brand").str

      brand ==> "monark"

    }
  }

  def withServer[T](m: cask.main.Main)(f: String => T): T = {
    val port = 8081
    val server = Undertow.builder
      .addHttpListener(port, "localhost")
      .setHandler(m.defaultHandler)
      .build
    server.start()
    val res =
      try f(s"http://localhost:$port")
      finally server.stop()
    res
  }
}
