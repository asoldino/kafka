package it.sol.kafka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import it.sol.kafka.actors.EventSourceActor
import it.sol.kafka.actors.EventSourceActor.{Create, Decrement, Increment, Update}
import it.sol.kafka.utils.KafkaProducer

/**
  * Created by andreasoldino on 2/25/17.
  */
object Main extends App {
  implicit val actorSystem = ActorSystem("actor-system")
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  val kafkaProducerActor = actorSystem.actorOf(KafkaProducer.props(ConfigFactory.load().getConfig("producer")))
  val eventSourceActor = actorSystem.actorOf(EventSourceActor.props(kafkaProducerActor, "Test-Aggregate"))

  val accepted = HttpResponse(StatusCodes.Accepted)
  val route: Route = (path(("create") / Segment) & get) { id =>
    eventSourceActor ! Create(id)
    complete(accepted)
  } ~ (path(("increment") / Segment) & get) { id =>
    eventSourceActor ! Increment(id)
    complete(accepted)
  } ~ (path(("decrement") / Segment) & get) { id =>
    eventSourceActor ! Decrement(id)
    complete(accepted)
  } ~ (path("set" / Segment / IntNumber) & get) { (id, value) =>
    eventSourceActor ! Update(id, value)
    complete(accepted)
  }

  Http().bindAndHandle(route, "localhost", 8080)
}
