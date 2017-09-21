package com.com.cluster.transformationmessages

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
  * Created by chandlerzhao on 2017/9/21.
  */
object BackendRunner {

  def main(args: Array[String]) {
    println("Start transformationBackend")
    val config = ConfigFactory
      .parseResources("node1.conf")
    val system = ActorSystem("metadataAkkaSystem", config)
    println("Started transformationBackend")
  }

}
