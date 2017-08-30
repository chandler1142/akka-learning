package com.akkademy

import akka.actor.Actor
import akka.event.Logging
import com.akkademy.messages.SetRequest

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
    }
    case o => log.info("received unknow message: {}", o)
  }
}
