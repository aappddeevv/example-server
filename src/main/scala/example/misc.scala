package example

import scala.annotation.implicitNotFound
import scala.annotation.migration
import scala.concurrent.Future
import scala.language._

import spray.json.DefaultJsonProtocol

case class Customer(id: Int, name: String, address: String)

// I use sleep which probably does not do what I want
// exactly given how actors work but is good enough for this example.
trait CustomerRespository extends DefaultJsonProtocol {

  private val r = scala.util.Random

  private val customers = Map(
    1 -> Customer(1, "John", "123 Play St."),
    2 -> Customer(2, "Mary", "495 Norm St."),
    3 -> Customer(3, "Beth", "391 Happy St."))

  /** Get a customer, pretend it has latency by using a Future ... */
  def find(id: Int)(implicit ec: concurrent.ExecutionContext) = Future {
    Thread.sleep(r.nextInt(4000))
    customers.get(id)
  }

  /** Return all the IDs known in the customer database ... */
  def ids(implicit ec: concurrent.ExecutionContext) = Future {
    Thread.sleep(r.nextInt(2000))
    customers.keys.toList
  }

  implicit val customerFormat = jsonFormat3(Customer)
  implicit val customerListFormt = immSeqFormat[Customer]
}

case class Msg(from: Int, to: Int, content: String)

trait MsgRepository extends DefaultJsonProtocol {

  private val r = scala.util.Random

  private val msgs = collection.immutable.Seq(
    Msg(1, 3, "yt?"),
    Msg(1, 1, "hi me!"),
    Msg(2, 1, "can't talk now"),
    Msg(2, 3, "how's it going?"),
    Msg(2, 3, "let's do it!"))

  /** Return a list of posts mode by a customer. */
  def getMsgsFrom(id: Int)(implicit ec: concurrent.ExecutionContext) =
    Future {
      Thread.sleep(r.nextInt(3000))
      msgs.filter(_.from == id)
    }

  /** Return a list of posts from a customer. */
  def getMsgsTo(id: Int)(implicit ec: concurrent.ExecutionContext) = Future {
      Thread.sleep(r.nextInt(3000))
      msgs.filter(_.to == id)
    }

  implicit val msgsFormat = jsonFormat3(Msg)
  implicit val msgListFormat = immSeqFormat[Msg]
}