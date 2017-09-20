package com.com.cluster.transformationmessages

import java.util

import akka.actor.{Actor, ActorRef}
import akka.cluster.Reachability.Terminated
import akka.event.Logging

/**
  * Created by chandlerzhao on 2017/9/21.
  */
class TransformationFrontend extends Actor {

  val log = Logging(context.system, this)

  val backends = new util.ArrayList[ActorRef]
  var jobCounter = 0

  override def receive: Receive = {
    case TransformationJob(text) if backends.isEmpty => {
      sender ! (new JobFailed("Service unavailable, try again later", new TransformationJob(text)))
    }
    case TransformationJob(text) => {
      jobCounter += 1
      backends.get(jobCounter % backends.size).forward(new TransformationJob(text))
    }
    case TransformationMessages.BACKEND_REGISTRATION => {
      context.watch(sender)
      backends.add(sender)
    }
    case Terminated => {
      backends.remove(sender)
    }
    case _ => {
      log.info("unknown messages")
    }
  }
}
