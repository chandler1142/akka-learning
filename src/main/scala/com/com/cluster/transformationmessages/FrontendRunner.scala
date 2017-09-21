package com.com.cluster.transformationmessages

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.Duration

/**
  * Created by chandlerzhao on 2017/9/21.
  */
object FrontendRunner {
  def main(args: Array[String]) {
    println("Start TransformationFrontend")
    val config = ConfigFactory
      .parseResources("node3.conf")
    val system = ActorSystem("metadataAkkaSystem", config)
    val transformationFrontend = system.actorOf(Props[TransformationFrontend], "transformationFrontend")

    val interval = Duration.create(2, TimeUnit.SECONDS)
    val counter = new AtomicInteger
    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    implicit val dispatcher = system.dispatcher

    system.scheduler.schedule(interval, interval, new Runnable {
      override def run(): Unit = {
        transformationFrontend ? new TransformationJob("hello-" + counter.getAndIncrement())
      }
    })

    println("Started TransformationFrontend")
  }
}
