package com.akka.cluster.simpleclusterlistener

import akka.actor.Actor
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, ClusterEvent}
import akka.event.Logging

/**
  * Created by chandlerzhao on 2017/9/21.
  */
class SimpleClusterListener extends Actor {

  val log = Logging(context.system, this)
  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    log.info("preStart")
    cluster.subscribe(
      self,
      initialStateMode = InitialStateAsEvents,
      classOf[ClusterEvent.MemberUp],
      classOf[ClusterEvent.MemberRemoved],
      classOf[UnreachableMember]
    )
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  override def receive = {
    case MemberUp(member) => {
      log.info("Member is Up: {}", member)
      println("Member is Up: {}", member)
    }
    case MemberRemoved(member, memberStatus) => {
      log.info("Member is Removed: {}, status: {}", member, memberStatus)
      println("Member is Removed: {}, status: {}", member, memberStatus)
    }
    case UnreachableMember(mUnreachable) => {
      log.info("Member is Unreachable: {}", mUnreachable)
      println("Member is Unreachable: {}", mUnreachable)
    }
    case CurrentClusterState(members, unreachable, seenBy, leader, roleLeaderMap) => {
      log.info("members: {}, unreachable: {}, seenBy: {}, leader: {}, roleLeaderMap: {}")
    }
    case _ => {
      log.info("unknown messages")
    }
  }

}
