package com.akkademy

import akka.actor.ActorSystem
import akka.pattern._
import akka.util.Timeout
import com.akkademy.messages.{GetRequest, SetRequest}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

/**
  * Created by chandlerzhao on 2017/9/20.
  */
class SClient(remoteAddress: String) {
  private implicit val timeout = Timeout(2 seconds)

  private val config = ConfigFactory
    .parseResources("application.conf")
    .getConfig("RemoteClientSideConfig")
  private implicit val system = ActorSystem("akkademy", config)

  private val remoteDb = system.actorSelection(s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db")

  def set(key: String, value: Object) = {
    remoteDb ? SetRequest(key, value)
  }

  def get(key: String) = {
    remoteDb ? GetRequest(key)
  }
}

