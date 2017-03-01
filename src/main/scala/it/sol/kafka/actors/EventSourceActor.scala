package it.sol.kafka.actors

/**
  * Created by andreasoldino on 2/25/17.
  */

object EventSourceActor {
  sealed trait Command extends Product with Serializable
  case class Create(id: String) extends Command
  case class Update(id: String, value: Integer) extends Command
  case class Increment(id: String) extends Command
  case class Decrement(id: String) extends Command
}
