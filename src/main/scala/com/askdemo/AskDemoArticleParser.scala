package com.askdemo

import akka.actor.Actor
import akka.pattern._
import akka.util.Timeout
import com.akkademy.messages.{GetRequest, SetRequest}

import scala.concurrent.Future

/**
  * Created by chandlerzhao on 2017/9/30.
  */
class AskDemoArticleParser(
                            cacheActorPath: String,
                            httpClientActorPath: String,
                            articleParserActorPath: String,
                            implicit val timeout: Timeout
                          ) extends Actor {

  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)

  override def receive: Receive = {
    case ParseArticle(uri) =>
      val senderRef = sender()
      val cacheResult = cacheActor ? GetRequest(uri)
      val result = cacheResult recoverWith {
        case _: Exception =>
          val fRawResult = httpClientActor ? uri

          fRawResult flatMap {
            case HttpResponse(rawArticle) =>
              articleParserActor ? ParseHtmlArticle(uri, rawArticle)
            case x =>
              Future.failed(new Exception("unknown response"))
          }
      }

      result onComplete {
        case scala.util.Success(x: String) =>
          println("cached result!")
          senderRef ! x
        case scala.util.Success(x: ArticleBody) =>
          cacheActor ! SetRequest(uri, x.body)
          senderRef ! x
        case scala.util.Failure(t) =>
          senderRef ! akka.actor.Status.Failure(t)
        case x =>
          println("unknown messages! " + x)
      }
  }
}
