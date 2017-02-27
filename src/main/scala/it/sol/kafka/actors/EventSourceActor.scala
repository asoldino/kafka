package it.sol.kafka.actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import cakesolutions.kafka.akka.ProducerRecords
import it.sol.kafka.actors.EventSourceActor.Command

/**
  * Created by andreasoldino on 2/25/17.
  */
class EventSourceActor(kafkaProducer: ActorRef, aggregateName: String) extends Actor with ActorLogging {
  override def receive: Receive = {
    case c: Command => kafkaProducer ! c.toString
  }
}

object EventSourceActor {
  sealed trait Command extends Product with Serializable
  case class Create(id: String) extends Command
  case class Update(id: String, value: Integer) extends Command
  case class Increment(id: String) extends Command
  case class Decrement(id: String) extends Command

  def props(kafkaProducer: ActorRef, aggregateName: String)(implicit actorSystem: ActorSystem): Props =
    Props.apply(new EventSourceActor(kafkaProducer, aggregateName))
}
