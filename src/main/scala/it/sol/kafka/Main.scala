package it.sol.kafka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import it.sol.kafka.utils.{KafkaConsumer, KafkaProducer}
import org.apache.kafka.clients.producer.ProducerRecord

/**
  * Created by andreasoldino on 2/25/17.
  */
object Main extends App with Directives {
  implicit val actorSystem = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()

  val accepted = HttpResponse(StatusCodes.Accepted)
  val route: Route = (path(("create") / Segment) & get) { id =>
    Source(1 to 10)
      .map(_.toString)
      .map { element =>
        new ProducerRecord[Array[Byte], String]("test-topic", element)
      }
      .runWith(Producer.plainSink(KafkaProducer.producerSettings))
    complete(accepted)
  } ~ (path(("increment") / Segment) & get) { id =>
    complete(accepted)
  } ~ (path(("decrement") / Segment) & get) { id =>
    complete(accepted)
  } ~ (path("set" / Segment / IntNumber) & get) { (id, value) =>
    complete(accepted)
  }

  Consumer.committableSource(KafkaConsumer.consumerSettings, Subscriptions.topics("test-topic"))
    .map(msg =>
      println(s"Received $msg")
    )
    .runWith(Sink.ignore)

  Http().bindAndHandle(route, "localhost", 8080)
}
