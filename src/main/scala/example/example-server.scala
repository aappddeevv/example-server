package example

import scala.io.StdIn
import scala.language._

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport.defaultNodeSeqMarshaller
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.Directive.addDirectiveApply
import akka.http.scaladsl.server.Directives._enhanceRouteWithConcatenation
import akka.http.scaladsl.server.Directives._segmentStringToPathMatcher
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.extractRequest
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.logRequestResult
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.Directives.pathEndOrSingleSlash
import akka.http.scaladsl.server.Directives.post
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.http.scaladsl.server.directives.LoggingMagnet.forRequestResponseFromMarker
import akka.stream.ActorMaterializer

object Args {
  @Parameter(names = Array("-h", "--help"), help = true)
  var help: Boolean = false
}

object Server extends LazyLogging {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val route =
    logRequestResult("example-server") {
      pathEndOrSingleSlash {
        get {
          complete(<h1>you have reached the example server</h1>)
        }
      } ~ path("echo") {
        get {
          extractRequest { req =>
            complete(req.toString)
          }
        }
      } ~ post {
        complete("POST succeeded")
      } ~ path("lines") { 
        get { 
          complete("1\n2\n3\n")
        }
      }
    }

  def main(args: Array[String]): Unit = {

    val j = new JCommander(Args, args.toArray: _*)
    if (Args.help) {
      val sb = new java.lang.StringBuilder()
      j.setProgramName("crmauth")
      j.usage(sb) // only way to get pre-formatted usage info
      sb.append("An application.conf can be use to specify some parameters.")
      println(sb.toString)
      system.terminate()
      return
    }

    val config = ConfigFactory.load()
    val ip = config.getString("http.host")
    val port = config.getInt("http.port")
    val server = ip + ":" + port

    val binding = Http().bindAndHandle(route, ip, port)
    binding onFailure {
      case ex: Exception =>
        logger.error(s"Error binding $server", ex)
        system.terminate()
        return
    }

    println(s"Started server on $server.\nPress ENTER to stop.")
    StdIn.readLine()

    binding flatMap { _.unbind() } onComplete { _ => system.terminate() }
  }
}
