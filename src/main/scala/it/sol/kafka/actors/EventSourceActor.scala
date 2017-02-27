package it.sol.kafka.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import it.sol.kafka.actors.EventSourceActor.Command
import cakesolutions.kafka.KafkaProducerRecord

/**
  * Created by andreasoldino on 2/25/17.
  */
class EventSourceActor(kafkaProducer: ActorRef, aggregateName: String) extends Actor with ActorLogging {
  override def receive: Receive = {
    case c: Command =>
      val msg = KafkaProducerRecord(aggregateName, None, c.toString)
      log.debug("Sending {} to producer", msg)
      kafkaProducer ! msg
  }
}

object EventSourceActor {
  sealed trait Command extends Product with Serializable
  case class Create(id: String) extends Command
  case class Update(id: String, value: Integer) extends Command
  case class Increment(id: String) extends Command
  case class Decrement(id: String) extends Command

  def props(kafkaProducer: ActorRef, aggregateName: String): Props =
    Props.apply(new EventSourceActor(kafkaProducer, aggregateName))
}
