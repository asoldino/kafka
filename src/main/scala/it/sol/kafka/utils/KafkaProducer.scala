package it.sol.kafka.utils

import cakesolutions.kafka.akka.KafkaProducerActor
import com.typesafe.config.Config
import org.apache.kafka.common.serialization.StringSerializer

/**
  * Created by andreasoldino on 2/26/17.
  */
object KafkaProducer {
  def props(config: Config) = KafkaProducerActor.props(config, new StringSerializer, new StringSerializer)
}