import upickle.default.{ReadWriter => RW, macroRW}
import upickle.default._
import java.time.Instant
import cask.router.NoOpParser
import scala.util.Random
import java.time.Clock
import shared.Protocol
import castor.SimpleActor
import cask.endpoints.WsChannelActor
import backend._

class WebServer() {}
object WebServer extends cask.Main {
  override def port: Int = 8384
  override def host: String = "0.0.0.0"
  val allRoutes = Seq(
    WebPageRoutes()
  )
  println(s"Starting/listening on $host:$port")
}

case class WebPageRoutes()(implicit cc: castor.Context, log: cask.Logger)
    extends cask.Routes {
  val bb = new InMemoryBicycleBackend

  @cask.get("/cycles")
  def list() = {
    val lista = bb.list()
    val cycles = upickle.default.writeJs(lista)
    ujson.Obj("cycles" -> cycles)
  }

  @cask.postJson("/cycles")
  def addBicycle(
      brand: ujson.Value,
      price: ujson.Value,
      stock: ujson.Value
  ) = {
    val b = Bicycle("", brand.str, price.num, stock.num.toInt)
    val bid = bb.addBicycle(b)
    ujson.Obj("bicycleId" -> bid)
  }

  @cask.postJson("/cycles/:bid/updateBrand")
  def update(bid: String, brand: ujson.Value) = {
    val b = bb.get(bid).get
    val updatedB = b.copy(brand = brand.str)
    bb.update(bid, updatedB)
    ujson.Obj("updated" -> true)
  }

  @cask.getJson("/cycles/:bid")
  def get(bid: String) = {
    val b = bb.get(bid)
    upickle.default.writeJs(b)
  }

  @cask.delete("/cycles/:bid")
  def delete(bid: String) = {
    bb.delete(bid)
    ujson.Obj("deleted" -> true)
  }

  /*  @cask.get("/cycles/:bid/searchByBrand")
  def searchByBrand(bid: String) = {
    val lista = bb.list()
    val listByBrand = lista.filter(b =>
      b.brand.toLowerCase
        .contains(bid.toLowerCase)
    )
    val searchedlist = upickle.default.writeJs(listByBrand)

    ujson.Obj("brands" -> searchedlist)
  }

  @cask.get("/cycles/searchByMaxPrice")
  def searchByMaxPrice(p: Double) = {
    val lista = bb.list()
    val listByPrice = lista.filter(b => b.price <= p)
    val maxPriceList = upickle.default.writeJs(listByPrice)

    ujson.Obj("price" -> maxPriceList)
  }

  @cask.get("/cycles/searchByPricenBrand")
  def searchByPricenBrand(p: Double) = {
    val lista = bb.list()
    val listaByBrand = lista.filter(b =>
      b.brand.toLowerCase
        .contains(bid.toLowerCase)
    )
    val byBrandnPrice = listaByBrand.filter(b => b.price <= p)
    val byBrandnPriceList = upickle.default.writeJs(byBrandnPrice)

    ujson.Obj("brandnprice" -> byBrandnPriceList)
  } */

  // hack to make cask serve the index.html page if browser requests subfolder /htm/about
  // fix this properly in nginx reverse proxy
  @cask.get("/htm", subpath = true)
  def pathWithDefaultPage(request: cask.Request) = {
    val filePath: String =
      if request.remainingPathSegments.isEmpty ||
        (!request.remainingPathSegments.last.endsWith(".js") &&
          !request.remainingPathSegments.last.endsWith(".js.map"))
      then "./vuegui/dist/index.html"
      else "./vuegui/dist/" + request.remainingPathSegments.mkString("/")
      end if

    if filePath.endsWith(".js") then
      val headers = Seq("Content-Type" -> "text/javascript")
      cask.model.StaticFile(filePath, headers)
    else if filePath.endsWith(".js.map") then
      val headers = Seq("Content-Type" -> "application/json")
      cask.model.StaticFile(filePath, headers)
    else if filePath.endsWith(".html") then
      val headers = Seq("Content-Type" -> "text/html")
      cask.model.StaticFile(filePath, headers)
    else
      val headers = Seq()
      cask.model.StaticFile(filePath, headers)

  }

  @cask.staticFiles(
    "/js/scala",
    headers = Seq("Content-Type" -> "text/javascript")
  )
  def staticFiles() = "out/js/fastOpt/dest"

  @cask.websocket("/connect/:userName")
  def connect(userName: String): cask.WebsocketResult = {
    if (userName == "haoyi") cask.Response("", statusCode = 403)
    else
      cask.WsHandler { channel =>
        Chat.send(ChatProtocol.NewMember(userName, channel))
        cask.WsActor {
          case cask.Ws.Text("") =>
            Chat.send(ChatProtocol.MemberLeving(userName))
            channel.send(cask.Ws.Close())
          case cask.Ws.Text(data) =>
            Chat.send(ChatProtocol.NewMessage(userName, data))
          case cask.Ws.Close(_, _) =>
            Chat.send(ChatProtocol.MemberLeving(userName))
        }
      }
  }

  object ChatProtocol {
    sealed trait Message
    case class NewMember(name: String, actor: WsChannelActor) extends Message
    case class NewMessage(name: String, actor: String) extends Message
    case class MemberLeving(name: String) extends Message
  }

  object Chat extends SimpleActor[ChatProtocol.Message] {
    import ChatProtocol._
    private var members: Map[String, WsChannelActor] = Map.empty

    override def run(msg: ChatProtocol.Message): Unit = msg match {
      case NewMember(name, actor) =>
        members = members + (name -> actor)
        broadcast(Protocol.Joined(name, members.keys.toSeq))
      case NewMessage(name, msg) =>
        broadcast(Protocol.ChatMessage(name, msg))
      case MemberLeving(name) =>
        members = members - name
        broadcast(Protocol.Left(name, members.keys.toSeq))
    }

    private def broadcast(msg: Protocol.Message): Unit = {
      val e = cask.Ws.Text(write(msg))
      members.values.foreach(_.send(e))
    }
  }

  initialize()
}
