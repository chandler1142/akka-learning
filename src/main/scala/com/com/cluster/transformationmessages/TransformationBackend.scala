package com.com.cluster.transformationmessages

import akka.actor.Actor
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberUp}
import akka.cluster.{Cluster, ClusterEvent, Member, MemberStatus}
import akka.event.Logging

/**
  * Created by chandlerzhao on 2017/9/21.
  */
class TransformationBackend extends Actor {

  val log = Logging(context.system, this)
  val cluster = Cluster.get(context.system)


  override def preStart = {
    cluster.subscribe(self, classOf[ClusterEvent.MemberUp])
  }

  override def postStop = {
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case TransformationJob(text) => {
      log.info("TransformationJob messages")
      log.info(text)
      sender ! new TransformationJob(text.toUpperCase())
    }
    case CurrentClusterState(members, unreachable, seenBy, leader, roleLeaderMap) => {
      log.info("CurrentClusterState messages")
      members.filter(m => m.status.equals(MemberStatus.Up)).map(m => {
        log.info("member status: {}", m.status)
        log.info("member role: {}", m.roles)
        register(m)
      })
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
