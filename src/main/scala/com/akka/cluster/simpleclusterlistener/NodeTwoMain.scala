package com.akka.cluster.simpleclusterlistener

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by chandlerzhao on 2017/9/21.
  */
object NodeTwoMain {
  def main(args: Array[String]) {
    println("Start simpleClusterListener");
    val config = ConfigFactory
      .parseResources("node2.conf")
    val system = ActorSystem("metadataAkkaSystem", config)
    system.actorOf(Props[SimpleClusterListener], "simpleClusterListener")
    println("Started simpleClusterListener");
  }
}
