package com.akkademy

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Status}
import akka.cluster.Cluster
import akka.event.Logging
import com.akkademy.messages.{GetRequest, KeyNotFoundException, SetRequest}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable

/**
  * Created by chandlerzhao on 2017/8/30.
  */
class AkkademyDb extends Actor {

  val map = new mutable.HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive = {
    case SetRequest(key, value) => {
      log.info("received SetRequest - key: {} vlaue: {}", key, value)
      map.put(key, value)
      sender ! Status.Success
    }
    case GetRequest(key) => {
      log.info("received GetRequest - key: {}", key)
      val response: Option[Object] =  map.get(key)
      response match {
        case Some(x) => {
          sender ! x
        }
        case None => sender ! Status.Failure(new KeyNotFoundException(key))
      }
    }
    case o => sender ! Status.Failure(new ClassNotFoundException)
  }
}

object AkkademyDb {

  def getActorRef: ActorRef = {
    val config = ConfigFactory
      .parseResources("application.conf")
      .getConfig("RemoteServerSideConfig")
    val system = ActorSystem("akkademy", config)
    //创建TearcherActor，返回一个引用
    //teacherActor 是 Actor 的名，客户端需要用
    system.actorOf(Props[AkkademyDb], "akkademy-db")

    val cluster = Cluster.get(system)

  }
}

