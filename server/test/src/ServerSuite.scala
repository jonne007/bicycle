import io.undertow.Undertow
import utest._

object ServerSuite extends TestSuite {
  val tests = Tests {
    test("list") - withServer(Server) { host =>
      val r = requests.get(host + "/cycles")
      val json = ujson.read(r.text())

      val cycles = json("cycles").arr

      cycles.size ==> 0
    }
    test("add and list") - withServer(Server) { host =>
      val createResponse =
        requests.post(
          url = host + "/cycles",
          data = """{"brand": "cresent", "price": 25, "stock": 12}"""
        )

      val json = ujson.read(createResponse.text())
      val id = json("id").str

      val r = requests.get(host + "/cycles")
      val json2 = ujson.read(r.text())

      val trainings = json2("cycles").arr

      trainings.size ==> 1
    }

    test("update") - withServer(Server) { host =>
      val createResponse =
        requests.post(
          url = host + "/cycles",
          data = """{"brand": "cresent"}"""
        )

      val json = ujson.read(createResponse.text())
      val id = json("id").str

      val r = requests.get(host + "/cycles")
      val json2 = ujson.read(r.text())

      val trainings = json2("cycles").arr

      trainings.size ==> 1
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
