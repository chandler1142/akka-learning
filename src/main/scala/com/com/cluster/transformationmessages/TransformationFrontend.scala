package com.com.cluster.transformationmessages

import akka.actor.{Actor, ActorRef, Terminated}
import akka.event.Logging

/**
  * Created by chandlerzhao on 2017/9/21.
  */
class TransformationFrontend extends Actor {

  val log = Logging(context.system, this)

  val backends = IndexedSeq.empty[ActorRef]
  var jobCounter = 0

  override def receive: Receive = {
    case TransformationJob(text) if backends.isEmpty => {
      log.info("TransformationJob init")
      sender ! (new JobFailed("Service unavailable, try again later", new TransformationJob(text)))
    }
    case TransformationJob(text) => {
      log.info("TransformationJob")
      jobCounter += 1
      backends(jobCounter % backends.size).forward(new TransformationJob(text))
    }
    case TransformationMessages.BACKEND_REGISTRATION => {
      log.info("BACKEND_REGISTRATION")
      context.watch(sender)
      backends :+ sender
    }
    case Terminated(a) => {
      log.info("Terminated")
      backends.filterNot(_==a)
    }
    case _ => {
      log.info("unknown messages")
    }
  }
}
