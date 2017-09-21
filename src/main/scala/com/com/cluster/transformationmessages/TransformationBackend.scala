package com.com.cluster.transformationmessages

import akka.actor.Actor
import akka.cluster.ClusterEvent.{CurrentClusterState, InitialStateAsEvents, MemberUp}
import akka.cluster.{Cluster, ClusterEvent, Member, MemberStatus}
import akka.event.Logging

/**
  * Created by chandlerzhao on 2017/9/21.
  */
class TransformationBackend extends Actor {

  val log = Logging(context.system, this)
  val cluster = Cluster.get(context.system)


  override def preStart = {
    log.info("backend is going to start...")
    cluster.subscribe(self, classOf[ClusterEvent.MemberUp])
  }

  override def postStop = {
    log.info("backend is going to stop...")
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case TransformationJob(text) => {
      log.info("TransformationJob messages")
      log.info(text)
      sender ! new TransformationJob(text.toUpperCase())
    }
    case state: CurrentClusterState => {
      log.info("CurrentClusterState messages")
      state.members.filter(_.status == MemberStatus.Up) foreach register
    }
    case MemberUp(m) => {
      log.info("MemberUp messages")
      register(m)
    }
    case _ => {
      log.info("unknown messages")
    }
  }

  def register(m: Member) = {
    if (m.hasRole("frontend")) {
      context.actorSelection(s"${m.address}/user/transformationFrontend") ! TransformationMessages.BACKEND_REGISTRATION
    }
  }

}
